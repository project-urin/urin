/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.CharacterSetMembershipFunction.*;

public abstract class Host {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(or(
            UNRESERVED,
            SUB_DELIMITERS
    ));

    public static Host registeredName(final String registeredName) {
        return new Host() {
            @Override
            public String asString() {
                return PERCENT_ENCODER.encode(registeredName);
            }
        };
    }

    public static Host ipV4Address(final DecimalOctet firstDecimalOctet, final DecimalOctet secondDecimalOctet, final DecimalOctet thirdDecimalOctet, final DecimalOctet fourthDecimalOctet) {
        return new Host() {
            @Override
            public String asString() {
                return new StringBuilder(firstDecimalOctet.asString())
                        .append('.')
                        .append(secondDecimalOctet.asString())
                        .append('.')
                        .append(thirdDecimalOctet.asString())
                        .append('.')
                        .append(fourthDecimalOctet.asString())
                        .toString();
            }
        };
    }

    public abstract String asString();

    private Host() {
    }
}
