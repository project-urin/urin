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

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static net.sourceforge.urin.AugmentedOptionalMatcher.populated;
import static net.sourceforge.urin.AugmentedOptionalMatcher.unpopulated;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AugmentedOptionalTest {

    @Test
    void emptyRejectsNullReason() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.empty(null));
    }

    @Test
    void ofRejectsNull() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.of(null));
    }

    @Test
    void emptyAugmentedOptionalOrRejectsNullAlternateOptionalSupplier() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.empty(aString()).or(null));
    }

    @Test
    void emptyAugmentedOptionalOrElseGetRejectsNullAlternateSupplier() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.empty(aString()).orElseGet(null));
    }

    @Test
    void emptyAugmentedOptionalOrElseThrowRejectsNullExceptionMapper() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.empty(aString()).orElseThrow(null));
    }

    @Test
    void emptyAugmentedOptionalFlatMapRejectsNullMapper() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.empty(aString()).orElseThrow(null));
    }

    @Test
    void emptyAugmentedOptionalOrSupplierOfNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.empty(aString()).or(() -> null));
    }

    @Test
    void emptyAugmentedOptionalOrPopulatedAugmentedOptionalReturnsThePopulatedAugmentedOptional() {
        final Object value = new Object();
        assertThat(AugmentedOptional.empty(aString()).or(() -> AugmentedOptional.of(value)), populated(sameInstance(value)));
    }

    @Test
    void emptyAugmentedOptionalOrEmptyAugmentedOptionalReturnsTheOriginalAugmentedOptional() {
        final AugmentedOptional<Object> emptyAugmentedOptional = AugmentedOptional.empty(aString());
        assertThat(emptyAugmentedOptional.or(() -> AugmentedOptional.empty(aString())), sameInstance(emptyAugmentedOptional));
    }

    @Test
    void emptyAugmentedOptionalOrElseGetReturnsAlternateValue() {
        final Object alternate = new Object();
        assertThat(AugmentedOptional.empty(aString()).orElseGet(() -> alternate), sameInstance(alternate));
    }

    @Test
    void emptyAugmentedOptionalOrElseGetReturnsNullAlternateValue() {
        assertThat(AugmentedOptional.empty(aString()).orElseGet(() -> null), is(nullValue()));
    }

    @Test
    void emptyAugmentedOptionalOrElseThrowThrowsNullPointerExceptionIfExceptionMapperReturnsNull() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.empty(aString()).orElseThrow(reason -> null));
    }

    @Test
    void emptyAugmentedOptionalOrElseThrowThrowsExceptionPopulatedWithReason() {
        final String reason = aString();
        final Exception thrownException = assertThrows(Exception.class, () -> AugmentedOptional.empty(reason).orElseThrow(Exception::new));
        assertThat(thrownException.getMessage(), equalTo(reason));
    }

    @Test
    void emptyAugmentedOptionalFlatMapReturnsEmptyAugmentedOptionalWithSameReason() {
        final String reason = aString();
        assertThat(AugmentedOptional.empty(reason).flatMap(value -> AugmentedOptional.of(new Object())), unpopulated(equalTo(reason)));
    }

    @Test
    void emptyAugmentedOptionalFlatMapDoesNotCallMapper() {
        AtomicInteger callCount = new AtomicInteger(0);
        AugmentedOptional.empty(aString()).flatMap(value -> {
            callCount.incrementAndGet();
            return AugmentedOptional.empty(aString());
        });
        assertThat("Mapper call count", callCount.get(), equalTo(0));
    }

    @Test
    void populatedAugmentedOptionalOrRejectsNullAlternateOptionalSupplier() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.of(new Object()).or(null));
    }

    @Test
    void populatedAugmentedOptionalOrElseGetRejectsNullAlternateSupplier() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.of(new Object()).orElseGet(null));
    }

    @Test
    void populatedAugmentedOptionalOrElseThrowRejectsNullExceptionMapper() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.of(new Object()).orElseThrow(null));
    }

    @Test
    void populatedAugmentedOptionalFlatMapRejectsNullMapper() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.of(new Object()).orElseThrow(null));
    }

    @Test
    void populatedAugmentedOptionalOrSupplierOfNullReturnsTheOriginalAugmentedOptional() {
        final AugmentedOptional<Object> populatedAugmentedOptional = AugmentedOptional.of(new Object());
        assertThat(populatedAugmentedOptional.or(() -> null), sameInstance(populatedAugmentedOptional));
    }

    @Test
    void populatedAugmentedOptionalOrPopulatedAugmentedOptionalReturnsTheOriginalAugmentedOptional() {
        final AugmentedOptional<Object> populatedAugmentedOptional = AugmentedOptional.of(new Object());
        assertThat(populatedAugmentedOptional.or(() -> AugmentedOptional.of(new Object())), sameInstance(populatedAugmentedOptional));
    }

    @Test
    void populatedAugmentedOptionalOrEmptyAugmentedOptionalReturnsTheOriginalAugmentedOptional() {
        final AugmentedOptional<Object> emptyAugmentedOptional = AugmentedOptional.of(new Object());
        assertThat(emptyAugmentedOptional.or(() -> AugmentedOptional.empty(aString())), sameInstance(emptyAugmentedOptional));
    }

    @Test
    void populatedAugmentedOptionalOrDoesNotCallSupplier() {
        AtomicInteger callCount = new AtomicInteger(0);
        AugmentedOptional.of(new Object()).or(() -> {
            callCount.incrementAndGet();
            return AugmentedOptional.empty(aString());
        });
        assertThat("Supplier call count", callCount.get(), equalTo(0));
    }

    @Test
    void populatedAugmentedOptionalOrElseGetReturnsTheOriginalValue() {
        final Object value = new Object();
        assertThat(AugmentedOptional.of(value).orElseGet(Object::new), sameInstance(value));
    }

    @Test
    void populatedAugmentedOptionalOrElseGetDoesNotCallSupplier() {
        AtomicInteger callCount = new AtomicInteger(0);
        AugmentedOptional.of(new Object()).orElseGet(() -> {
            callCount.incrementAndGet();
            return new Object();
        });
        assertThat("Supplier call count", callCount.get(), equalTo(0));
    }

    @Test
    void populatedAugmentedOptionalOrElseThrowThrowsReturnsTheOriginalValue() throws Exception {
        final Object value = new Object();
        assertThat(AugmentedOptional.of(value).orElseThrow(Exception::new), equalTo(value));
    }

    @Test
    void populatedAugmentedOptionalOrElseThrowDoesNotCallMapper() throws Exception {
        AtomicInteger callCount = new AtomicInteger(0);
        AugmentedOptional.of(new Object()).orElseThrow(reason -> {
            callCount.incrementAndGet();
            return new Exception(reason);
        });
        assertThat("Mapper call count", callCount.get(), equalTo(0));
    }

    @Test
    void populatedAugmentedOptionalFlatMapReturnsResultOfMapper() {
        final Object value = new Object();
        final AugmentedOptional<Object> mappedAugmentedOptional = AugmentedOptional.empty(aString());
        assertThat(AugmentedOptional.of(value).flatMap(actualValue -> {
            assertThat(actualValue, equalTo(value));
            return mappedAugmentedOptional;
        }), equalTo(mappedAugmentedOptional));
    }

    @Test
    void populatedAugmentedOptionalFlatMapThrowsNullPointerExceptionIfMapperReturnsNull() {
        assertThrows(NullPointerException.class, () -> AugmentedOptional.of(new Object()).flatMap(value -> null));
    }

}