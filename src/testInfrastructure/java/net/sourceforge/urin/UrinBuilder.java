/*
 * Copyright 2017 Mark Slater
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
import static net.sourceforge.urin.PathBuilder.*;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;

public class UrinBuilder {

    private static final RandomSupplierSwitcher<Urin<String, Query<String>, Fragment<String>>> RANDOM_UNPOLLUTED_URIN_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin();
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anUnpollutedPath());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anUnpollutedAbsolutePath());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anUnpollutedPath(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anUnpollutedAbsolutePath(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anUnpollutedPath(), aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anUnpollutedAbsolutePath(), aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aQuery(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anUnpollutedPath(), aQuery(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), aQuery(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anUnpollutedAbsolutePath(), aQuery(), aFragment());
                }
            }
    );

    private static final RandomSupplierSwitcher<Urin<String, Query<String>, Fragment<String>>> RANDOM_POLLUTED_URIN_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin();
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aPath());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aPath(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aPath(), aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath(), aQuery());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aQuery(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(aPath(), aQuery(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), aQuery(), aFragment());
                }
            },
            new Supplier<Urin<String, Query<String>, Fragment<String>>>() {
                public Urin<String, Query<String>, Fragment<String>> get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment());
                }
            }
    );

    public static Urin<String, Query<String>, Fragment<String>> aUrin() {
        return RANDOM_POLLUTED_URIN_SUPPLIER_SWITCHER.get();
    }

    public static Urin<String, Query<String>, Fragment<String>> anUnpollutedUrin() {
        return RANDOM_UNPOLLUTED_URIN_SUPPLIER_SWITCHER.get();
    }

}
