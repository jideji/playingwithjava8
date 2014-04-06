package se.dannej.except;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static se.dannej.except.Risky.givenRisky;
import static se.dannej.matcher.LambdaFeatureMatcher.feature;

public class ExpectTest {
    @Test
    public void test() {
	givenRisky(() -> {
	    throw new RuntimeException("later");
	}).expect(
		message(startsWith("l")),
		message(endsWith("r")));
    }

    private static Matcher<RuntimeException> message(Matcher<String> matcher) {
	return feature(matcher, "message", e -> e.getMessage());
    }
}
