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

import static net.sourceforge.urin.Octet.octet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OctetTest {

    @Test
    void asStringReturnsTheArgumentWhenUsingAnInt() {
        assertThat(octet(0).asString(), equalTo("0"));
        assertThat(octet(255).asString(), equalTo("255"));
    }

    @Test
    void rejectsIntArgumentBelowRange() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> octet(-1));
        assertThat(illegalArgumentException.getMessage(), equalTo("Argument must be in the range 0-255 but was [-1]"));
    }

    @Test
    void rejectsIntArgumentsAboveRange() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> octet(256));
        assertThat(illegalArgumentException.getMessage(), equalTo("Argument must be in the range 0-255 but was [256]"));
    }

    @Test
    void isValidAcceptsBoundaryCases() {
        assertThat(Octet.isValid("0"), equalTo(true));
        assertThat(Octet.isValid("255"), equalTo(true));
    }

    @Test
    void isValidRejectsOutsideBoundaryCases() {
        assertThat(Octet.isValid("-1"), equalTo(false));
        assertThat(Octet.isValid("256"), equalTo(false));
    }

    @Test
    void isValidRejectsNonIntegerCases() {
        assertThat(Octet.isValid("1.0"), equalTo(false));
        assertThat(Octet.isValid("Hello"), equalTo(false));
        assertThat(Octet.isValid(""), equalTo(false));
        assertThat(Octet.isValid(null), equalTo(false));
    }

    @Test
    void parseAcceptsBoundaryCases() throws Exception {
        assertThat(Octet.parse("0"), equalTo(octet(0)));
        assertThat(Octet.parse("255"), equalTo(octet(255)));
    }

    @Test
    void parseRejectsOctetStringOutsideRange() {
        final ParseException parseException = assertThrows(ParseException.class, () -> Octet.parse("-1"));
        assertThat(parseException.getMessage(), equalTo("Argument must be in the range 0-255 but was [-1]"));
    }

    @Test
    void parseRejectsNullOctetString() {
        final ParseException parseException = assertThrows(ParseException.class, () -> Octet.parse(null));
        assertThat(parseException.getMessage(), equalTo("Invalid Octet String [null]"));
    }
}
