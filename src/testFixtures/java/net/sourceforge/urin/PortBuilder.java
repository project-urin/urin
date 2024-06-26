/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.Port.port;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public final class PortBuilder {
    private PortBuilder() {
    }

    public static Port aPort() {
        return port(randomNumeric(5));
    }

    public static Port aPortDifferentTo(final Port port) {
        final String potentialPort = randomNumeric(5);
        return port(potentialPort).equals(port) ? port(potentialPort + randomNumeric(1)) : port(potentialPort);
    }
}
