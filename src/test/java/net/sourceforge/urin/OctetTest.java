/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.Test;

import static net.sourceforge.urin.Octet.octet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OctetTest {

    @Test
    public void asStringReturnsTheArgumentWhenUsingAnInt() throws Exception {
        assertThat(octet(0).asString(), equalTo("0"));
        assertThat(octet(255).asString(), equalTo("255"));
    }

    @Test
    public void rejectsIntArgumentsOutsideRange() throws Exception {
        try {
            octet(-1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0-255 but was [-1]"));
        }
        try {
            octet(256);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0-255 but was [256]"));
        }
    }

    @Test
    public void isValidAcceptsBoundaryCases() throws Exception {
        assertThat(Octet.isValid("0"), equalTo(true));
        assertThat(Octet.isValid("255"), equalTo(true));
    }

    @Test
    public void isValidRejectsOutsideBoundaryCases() throws Exception {
        assertThat(Octet.isValid("-1"), equalTo(false));
        assertThat(Octet.isValid("256"), equalTo(false));
    }

    @Test
    public void isValidRejectsNonIntegerCases() throws Exception {
        assertThat(Octet.isValid("1.0"), equalTo(false));
        assertThat(Octet.isValid("Hello"), equalTo(false));
        assertThat(Octet.isValid(""), equalTo(false));
        //noinspection NullableProblems
        assertThat(Octet.isValid(null), equalTo(false));
    }

    @Test
    public void parseAcceptsBoundaryCases() throws Exception {
        assertThat(Octet.parse("0"), equalTo(octet(0)));
        assertThat(Octet.parse("255"), equalTo(octet(255)));
    }

    @Test
    public void parseRejectsInvalidOctetStrings() throws Exception {
        try {
            Octet.parse("-1");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0-255 but was [-1]"));
        }
        try {
            //noinspection NullableProblems
            Octet.parse(null);
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Invalid Octet String [null]"));
        }
    }
}
