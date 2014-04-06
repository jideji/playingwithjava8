package se.dannej.concurrencies;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static java.util.stream.Collectors.toList;

public class Concurrent<F> {
    private Collection<F> coll;

    public Concurrent(Collection<F> coll) {
	this.coll = coll;
    }

    public static <F> Concurrent<F> concurrentlyOn(List<F> originalList) {
	return new Concurrent(originalList);
    }

    public <T> Stream<T> perform(Function<F, T> op) {
	return perform(op, commonPool());
    }

    public <T> Stream<T> perform(Function<F, T> op, ExecutorService services) {
	List<CompletableFuture<T>> results = coll.stream()
		.map(x -> supplyAsync(() -> op.apply(x), services))
		.collect(toList());

	return results.stream()
		.map(f -> f.join());
    }
}
