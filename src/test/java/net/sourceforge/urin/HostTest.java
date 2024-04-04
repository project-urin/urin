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

import java.util.Random;

import static java.util.Locale.US;
import static net.sourceforge.urin.CharacterSets.*;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.HexadectetBuilder.aHexadectet;
import static net.sourceforge.urin.HexadectetBuilder.aNonZeroHexadectet;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.HostBuilder.*;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.Octet.octet;
import static net.sourceforge.urin.OctetBuilder.anOctet;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("UnnecessaryLocalVariable")
class HostTest {

    private static final String IP_V_FUTURE_ADDRESS_CHARACTERS = LOWER_CASE_ALPHA + UPPER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":";
    private static final Random RANDOM = new Random();

    @Test
    void registeredNameAsStringReturnsValueProvidedForUnreservedCharacters() {
        String nonPercentEncodedCharacters = LOWER_CASE_ALPHA + UPPER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS;
        assertThat(registeredName(nonPercentEncodedCharacters).asString(), equalTo(LOWER_CASE_ALPHA + LOWER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS));
    }

    @Test
    void registeredNameLowerCasesUpperCaseNames() {
        assertThat(registeredName(UPPER_CASE_ALPHA).asString(), equalTo(LOWER_CASE_ALPHA));
    }

    @Test
    void registeredNameAsStringPercentEncodesNonUnreservedCharacters() {
        assertThat(registeredName(".:.@.#.[.]. .?.").asString(), equalTo(".%3A.%40.%23.%5B.%5D.%20.%3F."));
    }

    @Test
    void registeredNamesWithMatchingValuesAreEqual() {
        String registeredName = aString();
        assertThat(registeredName(registeredName), equalTo(registeredName(registeredName)));
        assertThat(registeredName(registeredName).hashCode(), equalTo(registeredName(registeredName).hashCode()));
    }

    @Test
    void registeredNamesWithDifferingValuesAreNotEqual() {
        assertThat(registeredName(aString()), not(equalTo(registeredName(aString()))));
    }

    @Test
    void registeredNameProducesCorrectToString() {
        String registeredName = aString();
        assertThat(registeredName(registeredName).toString(), equalTo("Host{registeredName='" + registeredName.toLowerCase(US) + "'}"));
    }

    @Test
    void rejectsNullInFactoryForARegisteredName() {
        assertThrows(NullPointerException.class, () -> registeredName(null), "Null registeredName should throw NullPointerException in factory");
    }

    @Test
    void parsesARegisteredName() throws Exception {
        Host host = aRegisteredName();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    void parsingAnEmptyRegisteredName() throws Exception {
        assertThat(parse(""), equalTo(registeredName("")));
    }

    @Test
    void parsingARegisteredNameWithNonPercentEncodedDisallowedCharactersThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("?"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :?"));
    }

    @Test
    void parsingAnRegisteredNameWithInvalidPercentEncodingThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("%F5"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :%F5"));
    }

    @Test
    void parsingAnRegisteredNameWithIncompletePercentEncodingThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("%2"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :%2"));
    }

    @Test
    void ipV4AddressAsStringIsCorrect() {
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo(firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString()));
    }

    @Test
    void ipV4AddressWithMatchingValuesAreEqual() {
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet), equalTo(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet)));
        assertThat(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).hashCode(), equalTo(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).hashCode()));
    }

    @Test
    void ipV4AddressCreatedWithIntegersIsEqualToOneCreatedWithEquivalentOctets() {
        int firstOctet = RANDOM.nextInt(256);
        int secondOctet = RANDOM.nextInt(256);
        int thirdOctet = RANDOM.nextInt(256);
        int fourthOctet = RANDOM.nextInt(256);
        assertThat(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet), equalTo(ipV4Address(octet(firstOctet), octet(secondOctet), octet(thirdOctet), octet(fourthOctet))));
    }

    @Test
    void ipV4AddressWithDifferingValuesAreNotEqual() {
        assertThat(ipV4Address(anOctet(), anOctet(), anOctet(), anOctet()), not(equalTo(ipV4Address(anOctet(), anOctet(), anOctet(), anOctet()))));
    }

    @Test
    void ipV4AddressProducesCorrectToString() {
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).toString(), equalTo("Host{firstOctet=" + firstOctet + ", secondOctet=" + secondOctet + ", thirdOctet=" + thirdOctet + ", fourthOctet=" + fourthOctet + "}"));
    }

    @Test
    void rejectsNullInFactoryForAnIpV4Address() {
        assertThrows(NullPointerException.class, () -> ipV4Address(null, anOctet(), anOctet(), anOctet()), "Null firstOctet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV4Address(anOctet(), null, anOctet(), anOctet()), "Null secondOctet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV4Address(anOctet(), anOctet(), null, anOctet()), "Null thirdOctet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV4Address(anOctet(), anOctet(), anOctet(), null), "Null fourthOctet should throw NullPointerException in factory");
    }

    @Test
    void parsesAnIpV4Address() throws Exception {
        Host host = anIpV4Address();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    void ipV6AddressAsStringIsCorrect() {
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
    void ipV6AddressAsStringDoesNotElideASingleZero() {
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
    void ipV6AddressAsStringElidesLongestSequenceOfZeros() {
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
    void ipV6AddressAsStringElidesSequenceOfZerosAtStart() {
        Hexadectet firstHexadectet = ZERO;
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = ZERO;
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Hexadectet seventhHexadectet = aNonZeroHexadectet();
        Hexadectet eighthHexadectet = aNonZeroHexadectet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).asString(),
                equalTo("[::" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + seventhHexadectet.asString() + ":" + eighthHexadectet.asString() + "]"));
    }

    @Test
    void ipV6AddressAsStringElidesSequenceOfZerosAtEnd() {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = aNonZeroHexadectet();
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = ZERO;
        Hexadectet seventhHexadectet = ZERO;
        Hexadectet eighthHexadectet = ZERO;
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + ":" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + "::]"));
    }

    @Test
    void ipV6AddressAsStringElidesSequenceOfAllZeros() {
        Hexadectet firstHexadectet = ZERO;
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = ZERO;
        Hexadectet fourthHexadectet = ZERO;
        Hexadectet fifthHexadectet = ZERO;
        Hexadectet sixthHexadectet = ZERO;
        Hexadectet seventhHexadectet = ZERO;
        Hexadectet eighthHexadectet = ZERO;
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).asString(),
                equalTo("[::]"));
    }

    @Test
    void ipV6AddressAsStringElidesFirstLongestSequenceOfZeros() {
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
    void ipV6AddressWithMatchingValuesAreEqual() {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = aNonZeroHexadectet();
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Hexadectet seventhHexadectet = aNonZeroHexadectet();
        Hexadectet eighthHexadectet = aNonZeroHexadectet();
        assertThat(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet), equalTo(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet)));
        assertThat(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).hashCode(), equalTo(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).hashCode()));
    }

    @Test
    void ipV6AddressWithDifferingValuesAreNotEqual() {
        assertThat(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet()), not(equalTo(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet()))));
    }

    @Test
    void ipV6AddressProducesCorrectToString() {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = aNonZeroHexadectet();
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Hexadectet seventhHexadectet = aNonZeroHexadectet();
        Hexadectet eighthHexadectet = aNonZeroHexadectet();
        assertThat(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet).toString(), equalTo("Host{firstHexadectet=" + firstHexadectet + ", secondHexadectet=" + secondHexadectet + ", thirdHexadectet=" + thirdHexadectet + ", fourthHexadectet=" + fourthHexadectet + ", fifthHexadectet=" + fifthHexadectet + ", sixthHexadectet=" + sixthHexadectet + ", seventhHexadectet=" + seventhHexadectet + ", eighthHexadectet=" + eighthHexadectet + "}"));
    }

    @Test
    void parsesAnIpV6Address() throws Exception {
        Host host = anIpV6Address();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    void parsesAnIpV6LoopbackAddress() throws Exception {
        assertThat(parse("[::1]"), equalTo(LOOPBACK_ADDRESS_IP_V6));
    }

    @Test
    void parsesAnIpV6UnspecifiedAddress() throws Exception {
        assertThat(parse("[::]"), equalTo(UNSPECIFIED_ADDRESS_IP_V6));
    }

    @Test
    void parsesRfc3515SampleIpV6Addresses() throws Exception {
        assertThat(parse("[1080::8:800:200C:417A]"), equalTo(ipV6Address(hexadectet(0x1080), ZERO, ZERO, ZERO, hexadectet(0x8), hexadectet(0x800), hexadectet(0x200C), hexadectet(0x417A))));
        assertThat(parse("[FF01::101]"), equalTo(ipV6Address(hexadectet(0xFF01), ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(0x101))));
        assertThat(parse("[0:0:0:0:0:0:13.1.68.3]"), equalTo(ipV6Address(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, octet(13), octet(1), octet(68), octet(3))));
        assertThat(parse("[0:0:0:0:0:FFFF:129.144.52.38]"), equalTo(ipV6Address(ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(0xFFFF), octet(129), octet(144), octet(52), octet(38))));
        assertThat(parse("[::13.1.68.3]"), equalTo(ipV6Address(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, octet(13), octet(1), octet(68), octet(3))));
        assertThat(parse("[::FFFF:129.144.52.38]"), equalTo(ipV6Address(ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(0xFFFF), octet(129), octet(144), octet(52), octet(38))));
        assertThat(parse("[12AB:0000:0000:CD30:0000:0000:0000:0000]"), equalTo(ipV6Address(hexadectet(0x12AB), ZERO, ZERO, hexadectet(0xCD30), ZERO, ZERO, ZERO, ZERO)));
        assertThat(parse("[12AB::CD30:0:0:0:0]"), equalTo(ipV6Address(hexadectet(0x12AB), ZERO, ZERO, hexadectet(0xCD30), ZERO, ZERO, ZERO, ZERO)));
        assertThat(parse("[12AB:0:0:CD30::]"), equalTo(ipV6Address(hexadectet(0x12AB), ZERO, ZERO, hexadectet(0xCD30), ZERO, ZERO, ZERO, ZERO)));
    }

    @Test
    void parsesAnIpV6AddressWithElidedParts() throws Exception {
        Host host = ipV6Address(
                aHexadectet(),
                ZERO,
                ZERO,
                aHexadectet(),
                aHexadectet(),
                aHexadectet(),
                aHexadectet(),
                aHexadectet()
        );
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    void parsingAnEmptyIpV6AddressThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[]"));
    }

    @Test
    void parsingASingleColonThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[:]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[:]"));
    }

    @Test
    void parsingAnIpV6AddressWithMultipleElidedPartsThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[1::1::1]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[1::1::1]"));
    }

    @Test
    void parsingAnIpV6AddressWithTooFewPartsThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[1:1:1]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[1:1:1]"));
    }

    @Test
    void parsingAnIpV6AddressWithTooManyPartsThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[1:2:3:4:5:6:7:8:9:10]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[1:2:3:4:5:6:7:8:9:10]"));
    }

    @Test
    void parsingAnIpV6AddressWithTooManyPartsIncludingElidedPartsThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[1::2:3:4:5:6:7:8:9:10]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[1::2:3:4:5:6:7:8:9:10]"));
    }

    @Test
    void parsingAnIpV6AddressWithPlusCharacterParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[1::+1:1]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[1::+1:1]"));
    }

    @Test
    void rejectsNullInFactoryForAnIpV6Address() {
        assertThrows(NullPointerException.class, () -> ipV6Address(null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet()), "Null firstHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet()), "Null secondHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet()), "Null thirdHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet()), "Null fourthHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet()), "Null fifthHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet()), "Null sixthHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet()), "Null seventhHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null), "Null eighthHexadectet should throw NullPointerException in factory");
    }

    @Test
    void ipV6AddressWithTrailingIpV4AddressAsStringIsCorrect() {
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
    void ipV6AddressWithTrailingIpV4AddressDoesNotElideASingleZero() {
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
    void ipV6AddressWithTrailingIpV4AddressElidesLongestSequenceOfZeros() {
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
    void ipV6AddressWithTrailingIpV4AddressAsStringElidesSequenceOfZerosAtStart() {
        Hexadectet firstHexadectet = ZERO;
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = ZERO;
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = aNonZeroHexadectet();
        Hexadectet sixthHexadectet = aNonZeroHexadectet();
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo("[::" + fourthHexadectet.asString() + ":" + fifthHexadectet.asString() + ":" + sixthHexadectet.asString() + ":" + firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString() + "]"));
    }

    @Test
    void ipV6AddressWithTrailingIpV4AddressAsStringElidesSequenceOfZerosAtEnd() {
        Hexadectet firstHexadectet = aNonZeroHexadectet();
        Hexadectet secondHexadectet = aNonZeroHexadectet();
        Hexadectet thirdHexadectet = aNonZeroHexadectet();
        Hexadectet fourthHexadectet = aNonZeroHexadectet();
        Hexadectet fifthHexadectet = ZERO;
        Hexadectet sixthHexadectet = ZERO;
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo("[" + firstHexadectet.asString() + ":" + secondHexadectet.asString() + ":" + thirdHexadectet.asString() + ":" + fourthHexadectet.asString() + "::" + firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString() + "]"));
    }

    @Test
    void ipV6AddressWithTrailingIpV4AddressAsStringElidesSequenceOfAllZeros() {
        Hexadectet firstHexadectet = ZERO;
        Hexadectet secondHexadectet = ZERO;
        Hexadectet thirdHexadectet = ZERO;
        Hexadectet fourthHexadectet = ZERO;
        Hexadectet fifthHexadectet = ZERO;
        Hexadectet sixthHexadectet = ZERO;
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(
                ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).asString(),
                equalTo("[::" + firstOctet.asString() + "." + secondOctet.asString() + "." + thirdOctet.asString() + "." + fourthOctet.asString() + "]"));
    }

    @Test
    void ipV6AddressWithTrailingIpV4AddressElidesFirstLongestSequenceOfZeros() {
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
    void ipV6AddressWithTrailingIpV4AddressWithMatchingValuesAreEqual() {
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
        assertThat(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet), equalTo(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet)));
        assertThat(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).hashCode(), equalTo(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).hashCode()));
    }

    @Test
    void ipV6AddressWithTrailingIpV4AddressWithDifferingValuesAreNotEqual() {
        assertThat(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()), not(equalTo(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()))));
    }

    @Test
    void ipV6AddressWithTrailingIpV4AddressProducesCorrectToString() {
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
        assertThat(ipV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet).toString(), equalTo("Host{firstHexadectet=" + firstHexadectet + ", secondHexadectet=" + secondHexadectet + ", thirdHexadectet=" + thirdHexadectet + ", fourthHexadectet=" + fourthHexadectet + ", fifthHexadectet=" + fifthHexadectet + ", sixthHexadectet=" + sixthHexadectet + ", firstOctet=" + firstOctet + ", secondOctet=" + secondOctet + ", thirdOctet=" + thirdOctet + ", fourthOctet=" + fourthOctet + "}"));
    }

    @Test
    void parsesAnIpV6AddressWithTrailingIpV4Address() throws Exception {
        Host host = anIpV6AddressWithTrailingIpV4Address();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    void parsesAnIpV6AddressWithTrailingIpV4AddressWithElidedParts() throws Exception {
        Host host = ipV6Address(
                aHexadectet(),
                ZERO,
                ZERO,
                aHexadectet(),
                aHexadectet(),
                aHexadectet(),
                anOctet(),
                anOctet(),
                anOctet(),
                anOctet()
        );
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    void parsingAnIpV6AddressWithTrailingIpV4AddressWithMultipleElidedPartsThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[1::1::1:1.1.1.1]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[1::1::1:1.1.1.1]"));
    }

    @Test
    void parsingAnIpV6AddressWithTrailingIpV4AddressWithTooFewPartsThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[1:1:1.1.1]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[1:1:1.1.1]"));
    }

    @Test
    void rejectsNullInFactoryForAnIpV6AddressWithTrailingIpV4Address() {
        assertThrows(NullPointerException.class, () -> ipV6Address(null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()), "Null firstHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()), "Null secondHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()), "Null thirdHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()), "Null fourthHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()), "Null fifthHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, anOctet(), anOctet(), anOctet(), anOctet()), "Null sixthHexadectet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, anOctet(), anOctet(), anOctet()), "Null firstOctet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), null, anOctet(), anOctet()), "Null secondOctet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), null, anOctet()), "Null thirdOctet should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), null), "Null fourthOctet should throw NullPointerException in factory");
    }

    @Test
    void ipVFutureAsStringIsCorrect() {
        String address = LOWER_CASE_ALPHA + UPPER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":";
        assertThat(
                ipVFutureAddress(HEX_DIGIT, address).asString(),
                equalTo("[v" + HEX_DIGIT + "." + LOWER_CASE_ALPHA + LOWER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":" + "]"));
    }

    @Test
    void ipVFutureRejectsEmptyVersion() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress("", aValidIpVFutureAddress()));
        assertThat(illegalArgumentException.getMessage(), equalTo("Version must contain at least one character"));
    }

    @Test
    void ipVFutureRejectsNullVersion() {
        assertThrows(NullPointerException.class, () -> ipVFutureAddress(null, aValidIpVFutureAddress()), "Null value should throw NullPointerException in factory");
    }

    @Test
    void parsesAnIpVFuture() throws Exception {
        Host host = anIpVFutureAddress();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    void parsesAnIpVFutureWithDotInAddress() throws Exception {
        assertThat(parse("[vABCDE.ab.ab]"), equalTo(ipVFutureAddress("ABCDE", "ab.ab")));
    }

    @Test
    void parsesAnIpVFutureWithLowerCaseHexVersion() throws Exception {
        assertThat(parse("[vabcde.ab.ab]"), equalTo(ipVFutureAddress("abcde", "ab.ab")));
    }

    @Test
    void parsingAnIpVFutureAddressWithNonHexVersionThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[vi.abc]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[vi.abc]"));
    }

    @Test
    void parsingAnIpVFutureAddressWithMissingVersionThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[v.abc]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[v.abc]"));
    }

    @Test
    void parsingAnIpVFutureAddressWithNoDotThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[v1abc]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[v1abc]"));
    }

    @Test
    void parsingAnIpVFutureAddressWithLessThanOneAddressCharacterThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[v1.]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[v1.]"));
    }

    @Test
    void parsingAnIpVFutureAddressWithAnInvalidAddressCharacterThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[v1.%]"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[v1.%]"));
    }

    @Test
    void parsingAnIpVFutureAddressWithTrailingKruftThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> parse("[vABCDE.foo]oops"));
        assertThat(parseException.getMessage(), equalTo("Not a valid host :[vABCDE.foo]oops"));
    }

    @Test
    void ipVFutureRejectsInvalidCharacterAInVersion() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress("g", aValidIpVFutureAddress()));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9, A-F, or a-f in version [g]"));
    }

    @Test
    void ipVFutureRejectsInvalidCharacterForwardsSlashInVersion() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress("/", aValidIpVFutureAddress()));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9, A-F, or a-f in version [/]"));
    }

    @Test
    void ipVFutureRejectsInvalidCharacterColonInVersion() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress(":", aValidIpVFutureAddress()));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9, A-F, or a-f in version [:]"));
    }

    @Test
    void ipVFutureRejectsInvalidCharacterAtInVersion() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress("@", aValidIpVFutureAddress()));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9, A-F, or a-f in version [@]"));
    }

    @Test
    void ipVFutureRejectsInvalidCharacterGInVersion() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress("G", aValidIpVFutureAddress()));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be 0-9, A-F, or a-f in version [G]"));
    }

    @Test
    void ipVFutureRejectsEmptyAddress() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress(aValidIpVFutureVersion(), ""));
        assertThat(illegalArgumentException.getMessage(), equalTo("Address must contain at least one character"));
    }

    @Test
    void ipVFutureRejectsNullAddress() {
        assertThrows(NullPointerException.class, () -> ipVFutureAddress(aValidIpVFutureVersion(), null), "Null value should throw NullPointerException in factory");
    }

    @Test
    void ipVFutureRejectsInvalidCharactersInAddress() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ipVFutureAddress(aValidIpVFutureVersion(), "/"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, A-Z, 0-9, -, ., _, ~, !, $, &, ', (, ), *, +, ,, ;, =, or : in address [/]"));
    }

    @Test
    void ipVFutureAddressWithMatchingValuesAreEqual() {
        String versionNumber = anIpVFutureAddressVersion();
        String address = anIpVFutureAddressAddress();
        assertThat(ipVFutureAddress(versionNumber, address), equalTo(ipVFutureAddress(versionNumber, address)));
        assertThat(ipVFutureAddress(versionNumber, address).hashCode(), equalTo(ipVFutureAddress(versionNumber, address).hashCode()));
    }

    @Test
    void ipVFutureAddressWithDifferingValuesAreNotEqual() {
        assertThat(ipVFutureAddress(anIpVFutureAddressVersion(), anIpVFutureAddressAddress()), not(equalTo(ipVFutureAddress(anIpVFutureAddressVersion(), anIpVFutureAddressAddress()))));
    }

    private static String anIpVFutureAddressVersion() {
        return random(5, HEX_DIGIT);
    }

    private static String anIpVFutureAddressAddress() {
        return random(5, IP_V_FUTURE_ADDRESS_CHARACTERS);
    }

    @Test
    void ipVFutureAddressProducesCorrectToString() {
        String versionNumber = anIpVFutureAddressVersion();
        String address = anIpVFutureAddressAddress();
        assertThat(ipVFutureAddress(versionNumber, address).toString(), equalTo("Host{version='" + versionNumber + "', address='" + address.toLowerCase(US) + "'}"));
    }

    @Test
    void rejectsNullInFactoryForAnIpVFutureAddress() {
        assertThrows(NullPointerException.class, () -> ipVFutureAddress(null, anIpVFutureAddressAddress()), "Null version should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> ipVFutureAddress(anIpVFutureAddressVersion(), null), "Null address should throw NullPointerException in factory");
    }

    @Test
    void localHostIsExpectedString() {
        assertThat(LOCAL_HOST.asString(), equalTo("localhost"));
    }

    @Test
    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    void loopbackIpV4AddressIsExpectedString() {
        assertThat(LOOPBACK_ADDRESS_IP_V4.asString(), equalTo("127.0.0.1"));
    }

    @Test
    void loopbackIpV6AddressIsExpectedString() {
        assertThat(LOOPBACK_ADDRESS_IP_V6.asString(), equalTo("[::1]"));
    }

    @Test
    void unspecifiedIpV6AddressIsExpectedString() {
        assertThat(UNSPECIFIED_ADDRESS_IP_V6.asString(), equalTo("[::]"));
    }

    @Test
    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    void aRegisteredNameThatIsAValidIpV4AddressIsEqualToTheEquivalentIpV4Address() {
        assertThat(registeredName("127.0.0.1"), equalTo(ipV4Address(octet(127), octet(0), octet(0), octet(1))));
    }

    @Test
    void parsingARegisteredNameThatIsAlmostAnIpV4AddressExceptForAPlusCharacterDoesNotMakeAnIpV4Address() {
        assertThat(registeredName("+2.0.0.1"), not(equalTo(ipV4Address(octet(2), octet(0), octet(0), octet(1)))));
    }

    private static String aValidIpVFutureAddress() {
        return LOWER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":";
    }

    private static String aValidIpVFutureVersion() {
        return HEX_DIGIT;
    }

}
