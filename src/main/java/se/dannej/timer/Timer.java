package se.dannej.timer;

import com.google.common.base.Stopwatch;

import java.util.function.Consumer;

import static com.google.common.base.Stopwatch.createStarted;

public class Timer {

    public static void time(Runnable toMeasure, Consumer<Stopwatch> timingConsumer) {
	Stopwatch stopwatch = createStarted();
	toMeasure.run();
	stopwatch.stop();

	timingConsumer.accept(stopwatch);
    }

    public static Consumer<Stopwatch> print() {
	return x -> System.out.println("Time taken: " + x);
    }
}
