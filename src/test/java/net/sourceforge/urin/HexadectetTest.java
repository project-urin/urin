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

import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.AugmentedOptionalMatcher.populated;
import static net.sourceforge.urin.AugmentedOptionalMatcher.unpopulated;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HexadectetTest {
    @Test
    void asStringReturnsGivenValue() {
        assertThat(hexadectet(0x0).asString(), equalTo("0"));
        assertThat(hexadectet(0xFFFF).asString(), equalTo("ffff"));
    }

    @Test
    void rejectsIntLessThanValidRange() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> hexadectet(-0x1));
        assertThat(illegalArgumentException.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [-0x1]"));
    }

    @Test
    void rejectsIntGreaterThanValidRange() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> hexadectet(0x10000));
        assertThat(illegalArgumentException.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [0x10000]"));
    }

    @Test
    void toStringIsCorrect() {
        assertThat(hexadectet(0xFA).toString(), equalTo("Hexadectet{value=0xFA}"));
    }

    @Test
    void parsesValidBoundaryHexadectetStrings() {
        assertThat(Hexadectet.parses(Integer.toString(0x0, 16)), populated(equalTo(hexadectet(0x0))));
        assertThat(Hexadectet.parses(Integer.toString(0xFFFF, 16)), populated(equalTo(hexadectet(0xFFFF))));
    }

    @Test
    void parsesLowerCaseAndUpperCaseHexadectetStrings() {
        assertThat(Hexadectet.parses("ffff"), populated(equalTo(hexadectet(0xFFFF))));
        assertThat(Hexadectet.parses("FFFF"), populated(equalTo(hexadectet(0xFFFF))));
    }

    @Test
    void parsesRejectsHexadectetStringsOutsideBoundary() {
        assertThat(Hexadectet.parses(Integer.toString(0x10000, 16)), unpopulated(equalTo("Argument must be in the range 0x0-0xFFFF but was [0x10000]")));
    }

    @Test
    void parsesRejectsNonHexDigitStrings() {
        assertThat(Hexadectet.parses("+1"), unpopulated(equalTo("Invalid Hexadectet String [+1]")));
        assertThat(Hexadectet.parses(Integer.toString(-0x1, 16)), unpopulated(equalTo("Invalid Hexadectet String [-1]")));
    }

    @Test
    void parsesRejectsInvalidHexadectetStrings() {
        assertThat(Hexadectet.parses("0.1"), unpopulated(equalTo("Invalid Hexadectet String [0.1]")));
        assertThat(Hexadectet.parses("hello"), unpopulated(equalTo("Invalid Hexadectet String [hello]")));
        assertThat(Hexadectet.parses(""), unpopulated(equalTo("Invalid Hexadectet String []")));
        assertThat(Hexadectet.parses(null), unpopulated(equalTo("Invalid Hexadectet String [null]")));
    }
}
