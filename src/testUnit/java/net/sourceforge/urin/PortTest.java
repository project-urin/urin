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

import static net.sourceforge.urin.CharacterSets.DIGIT;
import static net.sourceforge.urin.Port.parse;
import static net.sourceforge.urin.Port.port;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PortTest {
    @Test
    void allowsFullRangeOfValidCharacters() {
        port(DIGIT);
    }

    @Test
    void asStringReturnsGivenPort() {
        assertThat(port("123").asString(), equalTo("123"));
    }

    @Test
    void canMakeAPortUsingAPositiveInt() {
        assertThat(port(123).asString(), equalTo("123"));
    }

    @Test
    void cannotMakeAPortUsingANegativeInt() {
        assertThrows(IllegalArgumentException.class, () -> port(-123), "Negative port should throw exception in factory");
    }

    @Test
    void normalisesLeadingZerosOnPort() {
        assertThat(port("01").asString(), equalTo("1"));
    }

    @Test
    void rejectsNullPort() {
        assertThrows(NullPointerException.class, () -> port(null), "Null value should throw NullPointerException in factory");
    }

    @Test
    void rejectsInvalidCharacterA() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> port("a"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9 in port [a]"));
    }

    @Test
    void rejectsInvalidCharacterForwardsSlash() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> port("/"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9 in port [/]"));
    }

    @Test
    void rejectsInvalidCharacterColon() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> port(":"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9 in port [:]"));
    }

    @Test
    void parsesFullRangeOfValidCharacters() throws Exception {
        Port.parse(DIGIT);
    }

    @Test
    void parsedPortIsCorrect() throws Exception {
        assertThat(Port.parse("123"), equalTo(port("123")));
    }

    @Test
    void cannotParseAPortUsingANegativeInt() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("-123"));
        assertThat(parseException.getMessage(), equalTo("Character 1 must be 0-9 in port [-123]"));
    }

    @Test
    void normalisesLeadingZerosOnParsedPort() throws Exception {
        assertThat(Port.parse("01"), equalTo(port(1)));
    }

    @Test
    void parseRejectsInvalidCharacterA() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("a"));
        assertThat(parseException.getMessage(), equalTo("Character 1 must be 0-9 in port [a]"));
    }

    @Test
    void parseRejectsInvalidCharacterForwardsSlash() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("/"));
        assertThat(parseException.getMessage(), equalTo("Character 1 must be 0-9 in port [/]"));
    }

    @Test
    void parseRejectsInvalidCharacterColon() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse(":"));
        assertThat(parseException.getMessage(), equalTo("Character 1 must be 0-9 in port [:]"));
    }

}
