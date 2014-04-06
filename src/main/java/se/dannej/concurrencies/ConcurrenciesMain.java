package se.dannej.concurrencies;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static se.dannej.concurrencies.Concurrent.concurrentlyOn;
import static se.dannej.timer.Timer.time;

/**
 * Concurrently calls a remote resource CLIENT times.
 * <p>
 * A warm up is first performed.
 * Then the concurrent remote calls are performed SAMPLE_SIZE times.
 * The statistics are then printed to stdout.
 */
public class ConcurrenciesMain {
    private static final int CLIENTS = 10;
    private static final int SAMPLE_SIZE = 100000;

    public void demo(ExecutorService service) {
	Supplier<List<RemoteResource>> clients = () -> blockingClients();

	System.out.println("Warming up");
	warmUp(x -> {
	    concurrentlyOn(clients.get())
		    .perform(RemoteResource::perform, service)
		    .forEach(r -> {});
	});

	System.out.println("Performing test");
	List<Long> times = measure(() -> {
	    concurrentlyOn(clients.get())
		    .perform(RemoteResource::perform, service)
		    .forEach(r -> {});
	});

	printStatistics(times);
    }

    private void warmUp(IntConsumer action) {
	IntStream.rangeClosed(1, 1000)
		.forEach(action);
    }

    private List<Long> measure(Runnable toMeasure) {
	List<Long> times = new ArrayList<>();
	IntStream.rangeClosed(1, SAMPLE_SIZE)
		.forEach(x -> {
		    time(toMeasure,
			    l -> times.add(l.elapsed(MICROSECONDS)));
		});
	return times;
    }

    private void printStatistics(List<Long> times) {
	LongSummaryStatistics statistics = times.stream()
		.mapToLong(x -> x)
		.summaryStatistics();
	System.out.println("Min: " + statistics.getMin() + " µs");
	System.out.println("Max: " + statistics.getMax() + " µs");
	System.out.println("Average: " + statistics.getAverage() + " µs");
	System.out.println();
    }

    /**
     * Blocking clients wait for each other to finish
     */
    private List<RemoteResource> blockingClients() {
	CountDownLatch latch = new CountDownLatch(CLIENTS);
	return rangeClosed(1, CLIENTS)
		.mapToObj(x -> new BlockingRemoteResource(latch, x))
		.collect(toList());
    }

    /**
     * Non-blocking clients only wait for themselves to finish. Still using a count down latch,
     * so that the clients are as similar as possible
     */
    private List<RemoteResource> nonBlockingClients() {
	CountDownLatch latch = new CountDownLatch(1);
	return rangeClosed(1, CLIENTS)
		.mapToObj(x -> new BlockingRemoteResource(latch, x))
		.collect(toList());
    }

    public static void main(String[] args) {
	ExecutorService service = newFixedThreadPool(150);
	try {
	    new ConcurrenciesMain().demo(service);
	} finally {
	    service.shutdown();
	}
    }
}
