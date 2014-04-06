package se.dannej.matcher;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static se.dannej.matcher.LambdaFeatureMatcher.feature;

public class LambdaFeatureMatcherTest {
    @Test
    public void test() {
	assertThat("Hello!", stringLength(equalTo(6)));
    }

    private FeatureMatcher<String, Integer> stringLength(Matcher<Integer> lengthMatcher) {
	return feature(lengthMatcher, "length", s -> s.length());
    }
}
