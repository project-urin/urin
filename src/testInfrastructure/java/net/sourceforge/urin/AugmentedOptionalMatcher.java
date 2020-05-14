/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.anything;

final class AugmentedOptionalMatcher {

    private AugmentedOptionalMatcher() {
    }

    private static final class UnpopulatedException extends Exception {
        UnpopulatedException(final String message) {
            super(message);
        }
    }

    static <T> Matcher<AugmentedOptional<T>> populated() {
        return populated(anything());
    }

    static <T> Matcher<AugmentedOptional<T>> populated(final Matcher<? super T> populatedValueMatcher) {
        return new TypeSafeDiagnosingMatcher<AugmentedOptional<T>>() {
            @Override
            protected boolean matchesSafely(final AugmentedOptional<T> item, final Description mismatchDescription) {
                try {
                    final T populatedValue = item.orElseThrow(UnpopulatedException::new);
                    final boolean result = populatedValueMatcher.matches(populatedValue);
                    if (!result) {
                        mismatchDescription.appendText("populated value ");
                        populatedValueMatcher.describeMismatch(populatedValue, mismatchDescription);
                    }
                    return result;
                } catch (UnpopulatedException ignored) {
                    mismatchDescription.appendText("AugmentedOptional is empty");
                    return false;
                }
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("A populated AugmentedOptional with populated value ").appendDescriptionOf(populatedValueMatcher);
            }
        };
    }

    static <T> Matcher<AugmentedOptional<T>> unpopulated() {
        return unpopulated(Matchers.any(String.class));
    }

    static <T> Matcher<AugmentedOptional<T>> unpopulated(final Matcher<String> reasonMatcher) {
        return new TypeSafeDiagnosingMatcher<AugmentedOptional<T>>() {
            @Override
            protected boolean matchesSafely(final AugmentedOptional<T> item, final Description mismatchDescription) {
                try {
                    final T populatedValue = item.orElseThrow(UnpopulatedException::new);
                    mismatchDescription.appendText("was populated with value ").appendValue(populatedValue);
                    return false;
                } catch (UnpopulatedException unpopulatedException) {
                    final boolean matches = reasonMatcher.matches(unpopulatedException.getMessage());
                    if (!matches) {
                        mismatchDescription.appendText("reason was ");
                        reasonMatcher.describeMismatch(unpopulatedException.getMessage(), mismatchDescription);
                    }
                    return matches;
                }
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("An unpopulated AugmentedOptional with reason ").appendDescriptionOf(reasonMatcher);
            }
        };
    }

}
