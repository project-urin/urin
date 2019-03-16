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

import java.util.Random;

import static net.sourceforge.urin.Hexadectet.hexadectet;

public class HexadectetBuilder {

    private static final Random RANDOM = new Random();

    static Hexadectet aNonZeroHexadectet() {
        return hexadectet(RANDOM.nextInt(0xFFFF) + 1);
    }

    static Hexadectet aHexadectet() {
        return hexadectet(RANDOM.nextInt(0x10000));
    }
}
