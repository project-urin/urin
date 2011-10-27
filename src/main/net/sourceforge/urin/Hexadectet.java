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

public final class Hexadectet {

    public static final Hexadectet ZERO = hexadectet(0x0);

    private final String hexadecimalHexadectet;
    private final boolean isElidable;

    public static Hexadectet hexadectet(final int hexadectet) {
        if (hexadectet < 0x0 || hexadectet > 0xFFFF) {
            throw new IllegalArgumentException("Argument must be in the range 0x0-0xFFFF but was [" + hexadectet + "]");
        }
        return new Hexadectet(Integer.toHexString(hexadectet), hexadectet == 0);
    }

    private Hexadectet(final String hexadecimalHexadectet, final boolean isElidable) {
        this.hexadecimalHexadectet = hexadecimalHexadectet;
        this.isElidable = isElidable;
    }

    String asString() {
        return hexadecimalHexadectet;
    }

    boolean isElidable() {
        return isElidable;
    }
}
