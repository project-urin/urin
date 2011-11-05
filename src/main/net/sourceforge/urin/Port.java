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

import java.math.BigInteger;

import static net.sourceforge.urin.CharacterSetMembershipFunction.DIGIT;

public final class Port extends UnaryStringValue {

    private Port(final String port) {
        super(port);
    }

    public static Port port(final int port) {
        return port(Integer.toString(port));
    }

    public static Port port(final String port) {
        for (int i = 0; i < port.length(); i++) {
            if (!DIGIT.isMember(port.charAt(i))) {
                throw new IllegalArgumentException("Character " + (i + 1) + " must be " + DIGIT.describe() + " in port [" + port + "]");
            }
        }
        return new Port(port.isEmpty() ? port : new BigInteger(port).toString());
    }
}
