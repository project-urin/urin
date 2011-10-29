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

import org.junit.Test;

import static net.sourceforge.urin.CharacterSets.*;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.HexadectetBuilder.aNonZeroHexadectet;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.OctetBuilder.anOctet;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HostTest {

    @Test
    public void registeredNameAsStringReturnsValueProvidedForUnreservedCharacters() throws Exception {
        String nonPercentEncodedCharacters = LOWER_CASE_ALPHA + UPPER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS;
        assertThat(registeredName(nonPercentEncodedCharacters).asString(), equalTo(LOWER_CASE_ALPHA + LOWER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS));
    }

    @Test
    public void registeredNameLowerCasesUpperCaseNames() throws Exception {
        assertThat(registeredName(UPPER_CASE_ALPHA).asString(), equalTo(LOWER_CASE_ALPHA));
    }

    @Test
    public void registeredNameAsStringPercentEncodesNonUnreservedCharacters() throws Exception {
        assertThat(registeredName(".:.@.#.[.]. .").asString(), equalTo(".%3A.%40.%23.%5B.%5D.%20."));
    }

    @Test
    public void registeredNamesWithMatchingValuesAreEqual() throws Exception {
        String registeredName = randomAlphanumeric(5);
        assertThat(registeredName(registeredName), equalTo(registeredName(registeredName)));
        assertThat(registeredName(registeredName).hashCode(), equalTo(registeredName(registeredName).hashCode()));
    }

    @Test
    public void registeredNameProducesCorrectToString() throws Exception {
        String registeredName = randomAlphanumeric(5);
        assertThat(registeredName(registeredName).toString(), equalTo("Host{registeredName='" + registeredName + "'}"));
    }

    @Test
    public void registeredNameReturnsGivenValue() throws Exception {
        String registeredName = randomAlphanumeric(5);
        assertThat(registeredName(registeredName).hasRegisteredName(), equalTo(true));
        assertThat(registeredName(registeredName).registeredName(), equalTo(registeredName));
    }

    @Test
    public void ipV4AddressAsStringIsCorrect() throws Exception {
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo(firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString()));
    }

    @Test
    public void ipV6AddressAsStringIsCorrect() throws Exception {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = aNonZeroHexadectet();
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Hexadectet seventhHexadectet = aNonZeroHexadectet();
        Hexadectet eighthHexadectet = aNonZeroHexadectet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + ":" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + seventhHexadectet.asString() + ":" + eighthHexadectet.asString() + "]"));
    }

    @Test
    public void ipV6AddressAsStringDoesNotElideASingleZero() throws Exception {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Hexadectet seventhHexadectet = aNonZeroHexadectet();
        Hexadectet eighthHexadectet = aNonZeroHexadectet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + ":" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + seventhHexadectet.asString() + ":" + eighthHexadectet.asString() + "]"));
    }

    @Test
    public void ipV6AddressAsStringElidesLongestSequenceOfZeros() throws Exception {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = ZERO;
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = ZERO;
        Hexadectet sixthHexadectet = ZERO;
        Hexadectet seventhHexadectet = ZERO;
        Hexadectet eighthHexadectet = aNonZeroHexadectet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + ":" + fourthHexadectet.asString() + "::" + eighthHexadectet.asString() + "]"));
    }

    @Test
    public void ipV6AddressAsStringElidesFirstLongestSequenceOfZeros() throws Exception {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = ZERO;
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = ZERO;
        Hexadectet sixthHexadectet = ZERO;
        Hexadectet seventhHexadectet = aNonZeroHexadectet();
        Hexadectet eighthHexadectet = aNonZeroHexadectet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).asString(),
                equalTo("[" + firstHexadectet.asString() + "::" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + seventhHexadectet.asString() + ":" + eighthHexadectet.asString() + "]"));
    }

    @Test
    public void ipV6AddressWithTrailingIpV4AddressAsStringIsCorrect() throws Exception {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = aNonZeroHexadectet();
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + ":" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString() + "]"));
    }

    @Test
    public void ipV6AddressWithTrailingIpV4AddressDoesNotElideASingleZero() throws Exception {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + ":" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString() + "]"));
    }

    @Test
    public void ipV6AddressWithTrailingIpV4AddressElidesLongestSequenceOfZeros() throws Exception {
        Hexadectet firstHexadectet = ZERO;
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = ZERO;
        Hexadectet fifthHexadectet = ZERO;
        Hexadectet sixthHexadectet = ZERO;
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + "::" + firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString() + "]"));
    }

    @Test
    public void ipV6AddressWithTrailingIpV4AddressElidesFirstLongestSequenceOfZeros() throws Exception {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = ZERO;
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = ZERO;
        Hexadectet sixthHexadectet = ZERO;
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo("[" + firstHexadectet.asString() + "::" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString() + "]"));
    }

    @Test
    public void ipVFutureAsStringIsCorrect() throws Exception {
        String address = LOWER_CASE_ALPHA + UPPER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":";
        assertThat(
                ipVFutureAddress(HEX_DIGIT, address).asString(),
                equalTo("[v" + HEX_DIGIT + "." + LOWER_CASE_ALPHA + LOWER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":" + "]"));
    }

    @Test
    public void ipVFutureRejectsEmptyVersion() throws Exception {
        try {
            ipVFutureAddress("", aValidIpVFutureAddress());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Version must contain at least one character"));
        }
    }

    @Test
    public void ipVFutureRejectsNullVersion() throws Exception {
        try {
            //noinspection NullableProblems
            ipVFutureAddress(null, aValidIpVFutureAddress());
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expect to end up here
        }
    }

    @Test
    public void ipVFutureRejectsInvalidCharactersInVersion() throws Exception {
        try {
            ipVFutureAddress("a", aValidIpVFutureAddress());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9, or A-F in version [a]"));
        }
        try {
            ipVFutureAddress("/", aValidIpVFutureAddress());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9, or A-F in version [/]"));
        }
        try {
            ipVFutureAddress(":", aValidIpVFutureAddress());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9, or A-F in version [:]"));
        }
        try {
            ipVFutureAddress("@", aValidIpVFutureAddress());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9, or A-F in version [@]"));
        }
        try {
            ipVFutureAddress("G", aValidIpVFutureAddress());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9, or A-F in version [G]"));
        }
    }

    @Test
    public void ipVFutureRejectsEmptyAddress() throws Exception {
        try {
            ipVFutureAddress(aValidIpVFutureVersion(), "");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Address must contain at least one character"));
        }
    }

    @Test
    public void ipVFutureRejectsNullAddress() throws Exception {
        try {
            //noinspection NullableProblems
            ipVFutureAddress(aValidIpVFutureVersion(), null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expect to end up here
        }
    }

    @Test
    public void ipVFutureRejectsInvalidCharactersInAddress() throws Exception {
        try {
            ipVFutureAddress(aValidIpVFutureVersion(), "/");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, A-Z, 0-9, -, ., _, ~, !, $, &, ', (, ), *, +, ,, ;, =, or : in address [/]"));
        }
    }

    private static String aValidIpVFutureAddress() {
        return LOWER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":";
    }

    private static String aValidIpVFutureVersion() {
        return HEX_DIGIT;
    }

}
