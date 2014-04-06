package se.dannej.except;

import org.hamcrest.Matcher;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Given an exception is expected to be thrown, assert on the exception.
 */
public class Risky {
    private Runnable r;
    private RuntimeException exception;

    public Risky(Runnable r) {
	this.r = r;
    }

    public static Risky givenRisky(Runnable r) {
	return new Risky(r);
    }

    public Risky expect(Matcher<RuntimeException>... matchers) {
	for (Matcher<RuntimeException> matcher : matchers) {
	    assertThat(getException(), matcher);
	}
	return this;
    }

    private RuntimeException getException() {
	if (exception == null) {
	    try {
		r.run();
		fail("Expected exception");
	    } catch (RuntimeException e) {
		exception = e;
		return e;
	    }
	}
	return exception;
    }
}
