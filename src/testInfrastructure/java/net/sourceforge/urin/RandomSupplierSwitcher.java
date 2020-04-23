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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class RandomSupplierSwitcher<T> implements Supplier<T> {

    private static final Random RANDOM = new Random();

    private final List<Supplier<T>> suppliers;

    @SafeVarargs
    public RandomSupplierSwitcher(final Supplier<T> supplier, final Supplier<T>... suppliers) {
        final List<Supplier<T>> supplierList = new ArrayList<>(suppliers.length + 1);
        supplierList.add(supplier);
        supplierList.addAll(asList(suppliers));
        this.suppliers = unmodifiableList(supplierList);
    }

    @Override
    public T get() {
        return suppliers.get(RANDOM.nextInt(suppliers.size())).get();
    }
}
