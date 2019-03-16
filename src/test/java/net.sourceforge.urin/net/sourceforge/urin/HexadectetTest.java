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

import org.junit.Test;

import static net.sourceforge.urin.Hexadectet.hexadectet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HexadectetTest {
    @Test
    public void asStringReturnsGivenValue() throws Exception {
        assertThat(hexadectet(0x0).asString(), equalTo("0"));
        assertThat(hexadectet(0xFFFF).asString(), equalTo("ffff"));
    }

    @Test
    public void rejectsIntsOutsideValidRange() throws Exception {
        try {
            hexadectet(-0x1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [-0x1]"));
        }
        try {
            hexadectet(0x10000);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [0x10000]"));
        }
    }

    @Test
    public void toStringIsCorrect() throws Exception {
        assertThat(hexadectet(0xFA).toString(), equalTo("Hexadectet{value=0xFA}"));
    }

    @Test
    public void boundaryHexadectetStringsAreValid() throws Exception {
        assertThat(Hexadectet.isValid(Integer.toString(0x0, 16)), equalTo(true));
        assertThat(Hexadectet.isValid(Integer.toString(0xFFFF, 16)), equalTo(true));
    }

    @Test
    public void outsideBoundaryHexadectetStringsAreNotValid() throws Exception {
        assertThat(Hexadectet.isValid(Integer.toString(-0x1, 16)), equalTo(false));
        assertThat(Hexadectet.isValid(Integer.toString(0x10000, 16)), equalTo(false));
    }

    @Test
    public void invalidHexadectetStringsAreNotValid() throws Exception {
        assertThat(Hexadectet.isValid("0.1"), equalTo(false));
        assertThat(Hexadectet.isValid("hello"), equalTo(false));
        assertThat(Hexadectet.isValid(""), equalTo(false));
        //noinspection NullableProblems
        assertThat(Hexadectet.isValid(null), equalTo(false));
    }

    @Test
    public void parsesValidBoundaryHexadectetStrings() throws Exception {
        assertThat(Hexadectet.parse(Integer.toString(0x0, 16)), equalTo(hexadectet(0x0)));
        assertThat(Hexadectet.parse(Integer.toString(0xFFFF, 16)), equalTo(hexadectet(0xFFFF)));
    }

    @Test
    public void parsingOutsideBoundaryHexadectetStringsThrowsParseException() throws Exception {
        final String minusOneHexAsString = Integer.toString(-0x1, 16);
        try {
            Hexadectet.parse(minusOneHexAsString);
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [-0x1]"));
        }
        String tenThousandHexAsString = Integer.toString(0x10000, 16);
        try {
            Hexadectet.parse(tenThousandHexAsString);
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [0x10000]"));
        }
    }
}
