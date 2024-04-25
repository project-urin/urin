/*
 * Copyright 2024 Mark Slater
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
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowingOptionalTest {
    private static <T> Matcher<ThrowingOptional<T>> emptyThrowingOptional() {
        return new TypeSafeDiagnosingMatcher<>() {
            @Override
            protected boolean matchesSafely(final ThrowingOptional<T> item, final Description mismatchDescription) {
                final Object alternative = new Object();
                try {
                    final boolean result = item.map(ignored -> new Object()).orElseGet(() -> alternative).equals(alternative);
                    if (!result) {
                        mismatchDescription.appendText("ThrowingOptional is populated");
                    }
                    return result;
                } catch (ParseException e) {
                    throw new IllegalStateException("Should never get here", e);
                }
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("An empty ThrowingOptional");
            }
        };
    }

    @Test
    void ofNullableAcceptsNull() {
        assertThat(ThrowingOptional.ofNullable(null), is(not(nullValue())));
    }

    @Test
    void ofNullableAcceptsNonNull() {
        assertThat(ThrowingOptional.ofNullable(new Object()), is(not(nullValue())));
    }

    @Test
    void emptyThrowingOptionalRejectsNullFilter() {
        assertThrows(NullPointerException.class, () -> ThrowingOptional.ofNullable(null).filter(null));
    }

    @Test
    void emptyThrowingOptionalRejectsNullMapper() {
        assertThrows(NullPointerException.class, () -> ThrowingOptional.ofNullable(null).map(null));
    }

    @Test
    void emptyThrowingOptionalRejectsNullAlternateSupplier() {
        assertThrows(NullPointerException.class, () -> ThrowingOptional.ofNullable(null).orElseGet(null));
    }

    @Test
    void emptyThrowingOptionalReturnsAlternateValue() throws Exception {
        final Object alternative = new Object();
        assertThat(ThrowingOptional.ofNullable(null).orElseGet(() -> alternative), equalTo(alternative));
    }

    @Test
    void emptyThrowingOptionalReturnsAlternateNull() throws Exception {
        assertThat(ThrowingOptional.ofNullable(null).orElseGet(() -> null), is(nullValue()));
    }

    @Test
    void emptyThrowingOptionalFiltersToAnEmptyThrowingOptional() {
        assertThat(ThrowingOptional.ofNullable(null).filter(item -> false), is(emptyThrowingOptional()));
        assertThat(ThrowingOptional.ofNullable(null).filter(item -> true), is(emptyThrowingOptional()));
    }

    @Test
    void emptyThrowingOptionalMapsToAnEmptyThrowingOptional() throws Exception {
        assertThat(ThrowingOptional.ofNullable(null).map(item -> new Object()), is(emptyThrowingOptional()));
    }

    @Test
    void populatedThrowingOptionalRejectsNullFilter() {
        assertThrows(NullPointerException.class, () -> ThrowingOptional.ofNullable(new Object()).filter(null));
    }

    @Test
    void populatedThrowingOptionalRejectsNullMapper() {
        assertThrows(NullPointerException.class, () -> ThrowingOptional.ofNullable(new Object()).map(null));
    }

    @Test
    void populatedThrowingOptionalRejectsNullAlternateSupplier() {
        assertThrows(NullPointerException.class, () -> ThrowingOptional.ofNullable(new Object()).orElseGet(null));
    }

    @Test
    void populatedThrowingOptionalReturnsOriginalValue() throws Exception {
        final Object value = new Object();
        assertThat(ThrowingOptional.ofNullable(value).orElseGet(Object::new), equalTo(value));
    }

    @Test
    void populatedThrowingOptionalReturnsEmptyWhenFilterReturnsTrue() {
        assertThat(ThrowingOptional.ofNullable(new Object()).filter(item -> true), is(emptyThrowingOptional()));
    }

    @Test
    void populatedThrowingOptionalReturnsOriginalWhenFilterReturnsTrue() throws Exception {
        final Object value = new Object();
        assertThat(ThrowingOptional.ofNullable(value).filter(item -> false).orElseGet(Object::new), equalTo(value));
    }

    @Test
    void populatedThrowingOptionalMapsToOptionalOfMappedValue() throws Exception {
        final Object value = new Object();
        final Object output = new Object();
        assertThat(ThrowingOptional.ofNullable(value).map(item -> {
            assertThat("argument to map is original value", item, equalTo(value));
            return output;
        }).orElseGet(Object::new), is(output));
    }

    @Test
    void populatedThrowingOptionalMapsToEmptyThrowingOptionalWhenMapReturnsNull() throws Exception {
        final Object value = new Object();
        assertThat(ThrowingOptional.ofNullable(value).map(item -> {
            assertThat("argument to map is original value", item, equalTo(value));
            return null;
        }), is(emptyThrowingOptional()));
    }
}