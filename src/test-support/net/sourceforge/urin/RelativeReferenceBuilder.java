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
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static net.sourceforge.urin.SegmentsBuilder.anAbsoluteSegments;

public class RelativeReferenceBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<RelativeReference> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<RelativeReference>(
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference();
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(aFragment());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(aQuery());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(aSegments());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(aSegments(), aFragment());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(aSegments(), aQuery());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(aSegments(), aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority(), aFragment());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority(), aQuery());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority(), aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority(), anAbsoluteSegments());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority(), anAbsoluteSegments(), aFragment());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority(), anAbsoluteSegments(), aQuery());
                }
            },
            new Supplier<RelativeReference>() {
                public RelativeReference get() {
                    return relativeReference(anAuthority(), anAbsoluteSegments(), aQuery(), aFragment());
                }
            }
    );

    public static RelativeReference aRelativeReference() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }
}
