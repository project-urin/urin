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

import org.junit.jupiter.api.Test;

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
    void boundaryHexadectetStringsAreValid() {
        assertThat(Hexadectet.isValid(Integer.toString(0x0, 16)), equalTo(true));
        assertThat(Hexadectet.isValid(Integer.toString(0xFFFF, 16)), equalTo(true));
    }

    @Test
    void outsideBoundaryHexadectetStringsAreNotValid() {
        assertThat(Hexadectet.isValid(Integer.toString(-0x1, 16)), equalTo(false));
        assertThat(Hexadectet.isValid(Integer.toString(0x10000, 16)), equalTo(false));
    }

    @Test
    void invalidHexadectetStringsAreNotValid() {
        assertThat(Hexadectet.isValid("0.1"), equalTo(false));
        assertThat(Hexadectet.isValid("hello"), equalTo(false));
        assertThat(Hexadectet.isValid(""), equalTo(false));
        assertThat(Hexadectet.isValid(null), equalTo(false));
    }

    @Test
    void parsesValidBoundaryHexadectetStrings() throws Exception {
        assertThat(Hexadectet.parse(Integer.toString(0x0, 16)), equalTo(hexadectet(0x0)));
        assertThat(Hexadectet.parse(Integer.toString(0xFFFF, 16)), equalTo(hexadectet(0xFFFF)));
    }

    @Test
    void parsingLessThanBoundaryHexadectetStringsThrowsParseException() {
        final String minusOneHexAsString = Integer.toString(-0x1, 16);
        final ParseException parseException = assertThrows(ParseException.class, () -> Hexadectet.parse(minusOneHexAsString));
        assertThat(parseException.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [-0x1]"));
    }

    @Test
    void parsingGreaterThanBoundaryHexadectetStringsThrowsParseException() {
        String tenThousandHexAsString = Integer.toString(0x10000, 16);
        final ParseException parseException = assertThrows(ParseException.class, () -> Hexadectet.parse(tenThousandHexAsString));
        assertThat(parseException.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [0x10000]"));
    }
}
