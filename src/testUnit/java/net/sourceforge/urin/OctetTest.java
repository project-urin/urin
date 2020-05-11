/*
 * Copyright 2020 Mark Slater
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
    void parsesAcceptsBoundaryCases() {
        assertThat(Octet.parses("0"), populated(equalTo(octet(0))));
        assertThat(Octet.parses("255"), populated(equalTo(octet(255))));
    }

    @Test
    void parsesRejectsOctetStringsOutsideBoundary() {
        assertThat(Octet.parses("256"), unpopulated(equalTo("Argument must be in the range 0-255 but was [256]")));
    }

    @Test
    void parsesRejectsNonDigitOctetStrings() {
        assertThat(Octet.parses("+1"), unpopulated(equalTo("Invalid Octet String [+1]")));
        assertThat(Octet.parses("-1"), unpopulated(equalTo("Invalid Octet String [-1]")));
    }

    @Test
    void parsesRejectsNonIntegerCases() {
        assertThat(Octet.parses("1.0"), unpopulated(equalTo("Invalid Octet String [1.0]")));
        assertThat(Octet.parses("Hello"), unpopulated(equalTo("Invalid Octet String [Hello]")));
        assertThat(Octet.parses(""), unpopulated(equalTo("Invalid Octet String []")));
        assertThat(Octet.parses(null), unpopulated(equalTo("Invalid Octet String [null]")));
    }

}
