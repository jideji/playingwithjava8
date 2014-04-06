package se.dannej.matcher;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.function.Function;

/**
 * A feature matcher that accepts lambda instead of expecting you to subclass it.
 *
 * @param <T> The type of the object to be matched
 * @param <U> The type of the feature to be matched
 */
public class LambdaFeatureMatcher<T, U> extends FeatureMatcher<T, U> {
    private final Function<T, U> function;

    /**
     * Constructor
     *
     * @param subMatcher         The matcher to apply to the feature
     * @param featureDescription Descriptive text to use in describeTo
     */
    private LambdaFeatureMatcher(Matcher<? super U> subMatcher, String featureDescription, String featureName, Function<T, U> function) {
	super(subMatcher, featureDescription, featureDescription);
	this.function = function;
    }

    public static <F, T> FeatureMatcher<F, T> feature(Matcher<? super T> subMatcher, String featureDescription, Function<F, T> function) {
	return new LambdaFeatureMatcher<>(subMatcher, featureDescription, featureDescription, function);
    }

    public static <F, T> FeatureMatcher<F, T> feature(Matcher<? super T> subMatcher, String featureDescription, String featureName, Function<F, T> function) {
	return new LambdaFeatureMatcher<>(subMatcher, featureDescription, featureName, function);
    }

    @Override
    protected U featureValueOf(T actual) {
	return function.apply(actual);
    }
}
