/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.function.Supplier;

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.PathBuilder.*;
import static net.sourceforge.urin.QueryBuilder.aQuery;

public class RelativeReferenceBuilder {

    private static final RandomSupplierSwitcher<RelativeReference<String, Query<String>, Fragment<String>>> RANDOM_POLLUTED_RELATIVE_REFERENCE_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference();
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aPath());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aPath(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aPath(), aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aPath(), aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anAbsolutePath());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anAbsolutePath(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment());
                }
            }
    );

    private static final RandomSupplierSwitcher<RelativeReference<String, Query<String>, Fragment<String>>> RANDOM_UNPOLLUTED_RELATIVE_REFERENCE_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference();
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anUnpollutedPath());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anUnpollutedPath(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anUnpollutedPath(), aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anUnpollutedPath(), aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), aQuery(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath(), aFragment());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath(), aQuery());
                }
            },
            new Supplier<RelativeReference<String, Query<String>, Fragment<String>>>() {
                public RelativeReference<String, Query<String>, Fragment<String>> get() {
                    return SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath(), aQuery(), aFragment());
                }
            }
    );

    public static RelativeReference<String, Query<String>, Fragment<String>> aRelativeReference() {
        return RANDOM_POLLUTED_RELATIVE_REFERENCE_SUPPLIER_SWITCHER.get();
    }

    public static RelativeReference<String, Query<String>, Fragment<String>> anUnpollutedRelativeReference() {
        return RANDOM_UNPOLLUTED_RELATIVE_REFERENCE_SUPPLIER_SWITCHER.get();
    }
}
