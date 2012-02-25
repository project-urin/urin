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

import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HierarchicalPartBuilder.aHierarchicalPart;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Urin.urin;

public class UrinBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<Urin> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<Urin>(
            new Supplier<Urin>() {
                public Urin get() {
                    return urin(aScheme(), aHierarchicalPart());
                }
            },
            new Supplier<Urin>() {
                public Urin get() {
                    return urin(aScheme(), aHierarchicalPart(), aFragment());
                }
            },
            new Supplier<Urin>() {
                public Urin get() {
                    return urin(aScheme(), aHierarchicalPart(), aQuery());
                }
            },
            new Supplier<Urin>() {
                public Urin get() {
                    return urin(aScheme(), aHierarchicalPart(), aQuery(), aFragment());
                }
            }
    );

    public static Urin aUrin() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

}
