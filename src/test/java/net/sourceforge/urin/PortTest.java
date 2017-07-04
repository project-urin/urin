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

import static net.sourceforge.urin.CharacterSets.DIGIT;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.Port.parse;
import static net.sourceforge.urin.Port.port;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PortTest {
    @Test
    public void allowsFullRangeOfValidCharacters() throws Exception {
        port(DIGIT);
    }

    @Test
    public void asStringReturnsGivenPort() throws Exception {
        assertThat(port("123").asString(), equalTo("123"));
    }

    @Test
    public void canMakeAPortUsingAPositiveInt() throws Exception {
        assertThat(port(123).asString(), equalTo("123"));
    }

    @Test
    public void cannotMakeAPortUsingANegativeInt() throws Exception {
        assertThrowsException("Negative port should throw exception in factory", IllegalArgumentException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws IllegalArgumentException {
                port(-123);
            }
        });
    }

    @Test
    public void normalisesLeadingZerosOnPort() throws Exception {
        assertThat(port("01").asString(), equalTo("1"));
    }

    @Test
    public void rejectsNullPort() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                port(null);
            }
        });
    }

    @Test
    public void rejectsInvalidCharacters() throws Exception {
        try {
            port("a");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [a]"));
        }
        try {
            port("/");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [/]"));
        }
        try {
            port(":");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [:]"));
        }
    }

    @Test
    public void parsesFullRangeOfValidCharacters() throws Exception {
        Port.parse(DIGIT);
    }

    @Test
    public void parsedPortIsCorrect() throws Exception {
        assertThat(Port.parse("123"), equalTo(port("123")));
    }

    @Test
    public void cannotParseAPortUsingANegativeInt() throws Exception {
        try {
            Port.parse("-123");
            fail("Expected a ParseException to be thrown");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [-123]"));
        }
    }

    @Test
    public void normalisesLeadingZerosOnParsedPort() throws Exception {
        assertThat(Port.parse("01"), equalTo(port(1)));
    }

    @Test
    public void parseRejectsInvalidCharacters() throws Exception {
        try {
            parse("a");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [a]"));
        }
        try {
            parse("/");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [/]"));
        }
        try {
            parse(":");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [:]"));
        }
    }

}
