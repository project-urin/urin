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

import static net.sourceforge.urin.CharacterSets.ALPHA;
import static net.sourceforge.urin.CharacterSets.DIGIT;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.Scheme.scheme;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class SchemeBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<Scheme> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<Scheme>(
            new Supplier<Scheme>() {
                public Scheme get() {
                    return aSchemeWithNoDefaultPort();
                }
            },
            new Supplier<Scheme>() {
                public Scheme get() {
                    return aSchemeWithDefaultPort();
                }
            }
    );


    static Scheme aScheme() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

    static Scheme aSchemeWithNoDefaultPort() {
        return scheme(aValidSchemeName());
    }

    static Scheme aSchemeWithDefaultPort() {
        return scheme(aValidSchemeName(), aPort());
    }

    static Scheme aSchemeWithDefaultPort(Port defaultPort) {
        return scheme(aValidSchemeName(), defaultPort);
    }

    static String aValidSchemeName() {
        return randomAlphabetic(1) + random(4, ALPHA + DIGIT + "+" + "-" + ".");
    }
}
