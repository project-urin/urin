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

import static net.sourceforge.urin.CharacterSets.ALPHA;
import static net.sourceforge.urin.CharacterSets.DIGIT;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.Scheme.scheme;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class SchemeBuilder {

    private static final RandomSupplierSwitcher<Scheme<String, Query<String>, Fragment<String>>> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            SchemeBuilder::aSchemeWithNoDefaultPort,
            SchemeBuilder::aSchemeWithDefaultPort
    );


    public static Scheme<String, Query<String>, Fragment<String>> aScheme() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

    static Scheme<String, Query<String>, Fragment<String>> aSchemeWithNoDefaultPort() {
        return scheme(aValidSchemeName());
    }

    static Scheme<String, Query<String>, Fragment<String>> aSchemeWithDefaultPort() {
        return scheme(aValidSchemeName(), aPort());
    }

    static Scheme<String, Query<String>, Fragment<String>> aSchemeWithDefaultPort(Port defaultPort) {
        return scheme(aValidSchemeName(), defaultPort);
    }

    static String aValidSchemeName() {
        return randomAlphabetic(1) + random(4, ALPHA + DIGIT + "+" + "-" + ".");
    }

}
