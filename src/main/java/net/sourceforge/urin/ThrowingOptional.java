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

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

abstract class ThrowingOptional<T> {
    private ThrowingOptional() {
    }

    static <T> ThrowingOptional<T> ofNullable(final T value) {
        if (value == null) {
            return new EmptyThrowingOptional<>();
        } else {
            return new PopulatedThrowingOptional<>(value);
        }
    }

    abstract ThrowingOptional<T> filter(Predicate<? super T> predicate);

    abstract <R> ThrowingOptional<R> map(ThrowingFunction<? super T, R> mapper) throws ParseException;

    abstract T orElseGet(ThrowingSupplier<T> alternateSupplier) throws ParseException;

    interface ThrowingFunction<FROM, TO> {
        TO apply(FROM from) throws ParseException;
    }

    interface ThrowingSupplier<T> {
        T get() throws ParseException;
    }

    private static final class PopulatedThrowingOptional<T> extends ThrowingOptional<T> {

        private final T value;

        PopulatedThrowingOptional(final T value) {
            this.value = value;
        }

        @Override
        ThrowingOptional<T> filter(final Predicate<? super T> predicate) {
            return predicate.test(value) ? new EmptyThrowingOptional<>() : this;
        }

        @Override
        <R> ThrowingOptional<R> map(final ThrowingFunction<? super T, R> mapper) throws ParseException {
            return ofNullable(mapper.apply(value));
        }

        @Override
        T orElseGet(final ThrowingSupplier<T> alternateSupplier) {
            requireNonNull(alternateSupplier, "alternateSupplier cannot be null");
            return value;
        }

    }

    private static final class EmptyThrowingOptional<T> extends ThrowingOptional<T> {
        @Override
        ThrowingOptional<T> filter(final Predicate<? super T> predicate) {
            requireNonNull(predicate, "predicate cannot be null");
            return this;
        }

        @Override
        <R> ThrowingOptional<R> map(final ThrowingFunction<? super T, R> mapper) {
            requireNonNull(mapper, "mapper cannot be null");
            return new EmptyThrowingOptional<>();
        }

        @Override
        T orElseGet(final ThrowingSupplier<T> alternateSupplier) throws ParseException {
            return alternateSupplier.get();
        }

    }
}
