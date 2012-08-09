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
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;

public class UrinBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<Urin<Query>> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<Urin<Query>>(
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin();
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(aPath());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(aFragment());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(aPath(), aFragment());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority(), aFragment());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath(), aFragment());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(aQuery());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(aPath(), aQuery());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority(), aQuery());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath(), aQuery());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(aQuery(), aFragment());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(aPath(), aQuery(), aFragment());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority(), aQuery(), aFragment());
                }
            },
            new Supplier<Urin<Query>>() {
                public Urin get() {
                    return aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment());
                }
            }
    );

    public static Urin<Query> aUrin() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

}
