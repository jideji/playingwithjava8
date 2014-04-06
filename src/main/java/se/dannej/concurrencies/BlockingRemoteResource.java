package se.dannej.concurrencies;

import java.util.concurrent.CountDownLatch;

public class BlockingRemoteResource implements RemoteResource {
    private CountDownLatch latch;
    private int x;

    public BlockingRemoteResource(CountDownLatch latch, int x) {
	this.latch = latch;
	this.x = x;
    }

    public int perform() {
	latch.countDown();
	try {
	    latch.await();
	} catch (InterruptedException e) {
	    throw new RuntimeException(e);
	}
	return x;
    }
}
