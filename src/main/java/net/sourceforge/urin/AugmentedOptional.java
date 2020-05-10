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

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

abstract class AugmentedOptional<T> {
    private AugmentedOptional() {
    }

    static <T> AugmentedOptional<T> empty(final String reason) {
        return new EmptyAugmentedOptional<>(requireNonNull(reason, "reason cannot be null"));
    }

    static <T> AugmentedOptional<T> of(final T value) {
        return new PopulatedAugmentedOptional<>(requireNonNull(value, "value cannot be null"));
    }

    abstract AugmentedOptional<T> or(Supplier<AugmentedOptional<? extends T>> alternateOptionalSupplier);

    abstract T orElseGet(Supplier<? extends T> alternateSupplier);

    abstract <EX extends Exception> T orElseThrow(Function<String, EX> exceptionMapper) throws EX;

    abstract <U> AugmentedOptional<U> flatMap(Function<T, ? extends AugmentedOptional<? extends U>> mapper);

    private static final class PopulatedAugmentedOptional<T> extends AugmentedOptional<T> {
        private final T value;

        PopulatedAugmentedOptional(final T value) {
            this.value = value;
        }

        @Override
        AugmentedOptional<T> or(final Supplier<AugmentedOptional<? extends T>> alternateOptionalSupplier) {
            requireNonNull(alternateOptionalSupplier, "alternateOptionalSupplier cannot be null");
            return this;
        }

        @Override
        T orElseGet(final Supplier<? extends T> alternateSupplier) {
            requireNonNull(alternateSupplier, "alternateSupplier cannot be null");
            return this.value;
        }

        @Override
        <EX extends Exception> T orElseThrow(final Function<String, EX> exceptionMapper) {
            requireNonNull(exceptionMapper, "exceptionMapper cannot be null");
            return value;
        }

        @Override
        <U> AugmentedOptional<U> flatMap(final Function<T, ? extends AugmentedOptional<? extends U>> mapper) {
            @SuppressWarnings("unchecked") final AugmentedOptional<U> result = (AugmentedOptional<U>) mapper.apply(value);
            return requireNonNull(result);
        }

    }

    private static final class EmptyAugmentedOptional<T> extends AugmentedOptional<T> {
        private final String reason;

        EmptyAugmentedOptional(final String reason) {
            this.reason = reason;
        }

        @Override
        AugmentedOptional<T> or(final Supplier<AugmentedOptional<? extends T>> alternateOptionalSupplier) {
            @SuppressWarnings("unchecked") final AugmentedOptional<T> result = (AugmentedOptional<T>) alternateOptionalSupplier.get();
            return requireNonNull(result).flatMap(value -> AugmentedOptional.of(AugmentedOptional.of(value))).orElseGet(() -> this);
        }

        @Override
        T orElseGet(final Supplier<? extends T> alternateSupplier) {
            return alternateSupplier.get();
        }

        @Override
        <EX extends Exception> T orElseThrow(final Function<String, EX> exceptionMapper) throws EX {
            throw exceptionMapper.apply(reason);
        }

        @Override
        <U> AugmentedOptional<U> flatMap(final Function<T, ? extends AugmentedOptional<? extends U>> mapper) {
            return AugmentedOptional.empty(reason);
        }

    }
}
