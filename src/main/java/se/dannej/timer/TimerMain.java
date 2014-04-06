package se.dannej.timer;

import com.google.common.base.Stopwatch;

import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static se.dannej.timer.Timer.print;
import static se.dannej.timer.Timer.time;

public class TimerMain {

    public static void main(String[] args) {
	time(() -> {
	    System.out.println("Computing best case scenario for deploying code. This may take a while...\n");
	    sleepSeconds(1);
	}, print().andThen(stat("stats.something")));
    }

    private static Consumer<Stopwatch> stat(String key) {
	return x -> System.out.println("Statting: '" + key + "'=" + x.elapsed(MILLISECONDS));
    }

    private static void sleepSeconds(int sleepTime) {
	try {
	    SECONDS.sleep(sleepTime);
	} catch (InterruptedException e) {
	    throw new RuntimeException(e);
	}
    }
}
