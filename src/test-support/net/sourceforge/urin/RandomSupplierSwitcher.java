/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import com.google.common.base.Supplier;

import java.util.Random;

import static java.lang.System.arraycopy;

public final class RandomSupplierSwitcher<T> implements Supplier<T> {

    private static final Random RANDOM = new Random();

    private final Supplier<T>[] suppliers;

    public RandomSupplierSwitcher(final Supplier<T> supplier, final Supplier<T>... suppliers) {
        //noinspection unchecked
        this.suppliers = new Supplier[suppliers.length + 1];
        this.suppliers[0] = supplier;
        arraycopy(suppliers, 0, this.suppliers, 1, suppliers.length);
    }

    public T get() {
        return suppliers[RANDOM.nextInt(suppliers.length)].get();
    }
}
