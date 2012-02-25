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

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static net.sourceforge.urin.SegmentsBuilder.anAbsoluteSegments;

public class HierarchicalPartBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<HierarchicalPart> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<HierarchicalPart>(
            new Supplier<HierarchicalPart>() {
                public HierarchicalPart get() {
                    return HierarchicalPart.hierarchicalPart();
                }
            },
            new Supplier<HierarchicalPart>() {
                public HierarchicalPart get() {
                    return aHierarchicalPartWithAuthorityAndNoPath();
                }
            },
            new Supplier<HierarchicalPart>() {
                public HierarchicalPart get() {
                    return aHierarchicalPartWithNoAuthority();
                }
            },
            new Supplier<HierarchicalPart>() {
                public HierarchicalPart get() {
                    return aHierarchicalPartAbsoluteWithAuthority();
                }
            }
    );

    public static HierarchicalPart aHierarchicalPart() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

    public static HierarchicalPart aHierarchicalPartWithAuthorityAndNoPath() {
        return hierarchicalPart(anAuthority());
    }

    public static HierarchicalPart aHierarchicalPartWithNoAuthority() {
        return hierarchicalPart(aSegments());
    }

    public static HierarchicalPart aHierarchicalPartAbsoluteWithAuthority() {
        return HierarchicalPart.hierarchicalPart(anAuthority(), anAbsoluteSegments());
    }

}
