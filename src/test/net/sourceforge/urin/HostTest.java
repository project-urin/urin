/*
 * Copyright 2012 Mark Slater
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
import static net.sourceforge.urin.HexadectetBuilder.aHexadectet;
import static net.sourceforge.urin.HexadectetBuilder.aNonZeroHexadectet;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.HostBuilder.*;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.OctetBuilder.anOctet;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HostTest {

    private static final String IP_V_FUTURE_ADDRESS_CHARACTERS = LOWER_CASE_ALPHA + UPPER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":";

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
        assertThat(registeredName(".:.@.#.[.]. .?.").asString(), equalTo(".%3A.%40.%23.%5B.%5D.%20.%3F."));
    }

    @Test
    public void registeredNamesWithMatchingValuesAreEqual() throws Exception {
        String registeredName = aString();
        assertThat(registeredName(registeredName), equalTo(registeredName(registeredName)));
        assertThat(registeredName(registeredName).hashCode(), equalTo(registeredName(registeredName).hashCode()));
    }

    @Test
    public void registeredNamesWithDifferingValuesAreNotEqual() throws Exception {
        assertThat(registeredName(aString()), not(equalTo(registeredName(aString()))));
    }

    @Test
    public void registeredNameProducesCorrectToString() throws Exception {
        String registeredName = aString();
        assertThat(registeredName(registeredName).toString(), equalTo("Host{registeredName='" + registeredName.toLowerCase() + "'}"));
    }

    @Test
    public void rejectsNullInFactoryForARegisteredName() throws Exception {
        assertThrowsNullPointerException("Null registeredName should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                registeredName(null);
            }
        });
    }

    @Test
    public void parsesARegisteredName() throws Exception {
        Host host = aRegisteredName();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    public void parsingAnEmptyRegisteredName() throws Exception {
        assertThat(parse(""), equalTo(registeredName("")));
    }

    @Test
    public void parsingARegisteredNameWithNonPercentEncodedDisallowedCharactersThrowsParseException() throws Exception {
        try {
            parse("?");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :?"));
        }
    }

    @Test
    public void parsingAnRegisteredNameWithInvalidPercentEncodingThrowsParseException() throws Exception {
        try {
            parse("%F5");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :%F5"));
        }
    }

    @Test
    public void parsingAnRegisteredNameWithIncompletePercentEncodingThrowsParseException() throws Exception {
        try {
            parse("%2");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :%2"));
        }
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
    public void ipV4AddressWithMatchingValuesAreEqual() throws Exception {
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet), equalTo(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet)));
        assertThat(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).hashCode(), equalTo(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).hashCode()));
    }

    @Test
    public void ipV4AddressWithDifferingValuesAreNotEqual() throws Exception {
        assertThat(ipV4Address(anOctet(), anOctet(), anOctet(), anOctet()), not(equalTo(ipV4Address(anOctet(), anOctet(), anOctet(), anOctet()))));
    }

    @Test
    public void ipV4AddressProducesCorrectToString() throws Exception {
        Octet firstOctet = anOctet();
        Octet secondOctet = anOctet();
        Octet thirdOctet = anOctet();
        Octet fourthOctet = anOctet();
        assertThat(ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).toString(), equalTo("Host{firstOctet=" + firstOctet + ", secondOctet=" + secondOctet + ", thirdOctet=" + thirdOctet + ", fourthOctet=" + fourthOctet + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAnIpV4Address() throws Exception {
        assertThrowsNullPointerException("Null firstOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV4Address(null, anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null secondOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV4Address(anOctet(), null, anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null thirdOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV4Address(anOctet(), anOctet(), null, anOctet());
            }
        });
        assertThrowsNullPointerException("Null fourthOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV4Address(anOctet(), anOctet(), anOctet(), null);
            }
        });
    }

    @Test
    public void parsesAnIpV4Address() throws Exception {
        Host host = anIpV4Address();
        assertThat(parse(host.asString()), equalTo(host));
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
    public void ipV6AddressWithMatchingValuesAreEqual() throws Exception {
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
    public void ipV6AddressWithDifferingValuesAreNotEqual() throws Exception {
        assertThat(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet()), not(equalTo(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet()))));
    }

    @Test
    public void ipV6AddressProducesCorrectToString() throws Exception {
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
    public void parsesAnIpV6Address() throws Exception {
        Host host = anIpV6Address();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    public void parsesAnIpV6AddressWithElidedParts() throws Exception {
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
    public void parsingAnIpV6HostWithMultipleElidedPartsThrowsParseException() throws Exception {
        try {
            parse("[1::1::1]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[1::1::1]"));
        }
    }

    @Test
    public void parsingAnIpV6HostWithTooFewPartsThrowsParseException() throws Exception {
        try {
            parse("[1:1:1]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[1:1:1]"));
        }
    }

    @Test
    public void rejectsNullInFactoryForAnIpV6Address() throws Exception {
        assertThrowsNullPointerException("Null firstHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet());
            }
        });
        assertThrowsNullPointerException("Null secondHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet());
            }
        });
        assertThrowsNullPointerException("Null thirdHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet());
            }
        });
        assertThrowsNullPointerException("Null fourthHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet());
            }
        });
        assertThrowsNullPointerException("Null fifthHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet());
            }
        });
        assertThrowsNullPointerException("Null sixthHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet());
            }
        });
        assertThrowsNullPointerException("Null seventhHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet());
            }
        });
        assertThrowsNullPointerException("Null eighthHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null);
            }
        });
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
    public void ipV6AddressWithTrailingIpV4AddressWithMatchingValuesAreEqual() throws Exception {
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
    public void ipV6AddressWithTrailingIpV4AddressWithDifferingValuesAreNotEqual() throws Exception {
        assertThat(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()), not(equalTo(ipV6Address(aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), aNonZeroHexadectet(), anOctet(), anOctet(), anOctet(), anOctet()))));
    }

    @Test
    public void ipV6AddressWithTrailingIpV4AddressProducesCorrectToString() throws Exception {
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
    public void parsesAnIpV6AddressWithTrailingIpV4Address() throws Exception {
        Host host = anIpV6AddressWithTrailingIpV4Address();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    public void parsesAnIpV6AddressWithTrailingIpV4AddressWithElidedParts() throws Exception {
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
    public void parsingAnIpV6AddressWithTrailingIpV4AddressWithMultipleElidedPartsThrowsParseException() throws Exception {
        try {
            parse("[1::1::1:1.1.1.1]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[1::1::1:1.1.1.1]"));
        }
    }

    @Test
    public void parsingAnIpV6AddressWithTrailingIpV4AddressWithTooFewPartsThrowsParseException() throws Exception {
        try {
            parse("[1:1:1.1.1]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[1:1:1.1.1]"));
        }
    }

    @Test
    public void rejectsNullInFactoryForAnIpV6AddressWithTrailingIpV4Address() throws Exception {
        assertThrowsNullPointerException("Null firstHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null secondHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null thirdHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null fourthHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null fifthHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, aHexadectet(), anOctet(), anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null sixthHexadectet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, anOctet(), anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null firstOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), null, anOctet(), anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null secondOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), null, anOctet(), anOctet());
            }
        });
        assertThrowsNullPointerException("Null thirdOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), null, anOctet());
            }
        });
        assertThrowsNullPointerException("Null fourthOctet should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), anOctet(), anOctet(), anOctet(), null);
            }
        });
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
    public void parsesAnIpVFuture() throws Exception {
        Host host = anIpVFutureAddress();
        assertThat(parse(host.asString()), equalTo(host));
    }

    @Test
    public void parsingAnIpVFutureAddressWithNonHexVersionThrowsParseException() throws Exception {
        try {
            parse("[vi.abc]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[vi.abc]"));
        }
        try {
            parse("[v.abc]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[v.abc]"));
        }
    }

    @Test
    public void parsingAnIpVFutureAddressWithNoDotThrowsParseException() throws Exception {
        try {
            parse("[viabc]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[viabc]"));
        }
    }

    @Test
    public void parsingAnIpVFutureAddressWithLessThanOneAddressCharacterThrowsParseException() throws Exception {
        try {
            parse("[v1.]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[v1.]"));
        }
    }

    @Test
    public void parsingAnIpVFutureAddressWithAnInvalidAddressCharacterThrowsParseException() throws Exception {
        try {
            parse("[v1.%]");
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            assertThat(e.getMessage(), equalTo("Not a valid host :[v1.%]"));
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

    @Test
    public void ipVFutureAddressWithMatchingValuesAreEqual() throws Exception {
        String versionNumber = anIpVFutureAddressVersion();
        String address = anIpVFutureAddressAddress();
        assertThat(ipVFutureAddress(versionNumber, address), equalTo(ipVFutureAddress(versionNumber, address)));
        assertThat(ipVFutureAddress(versionNumber, address).hashCode(), equalTo(ipVFutureAddress(versionNumber, address).hashCode()));
    }

    @Test
    public void ipVFutureAddressWithDifferingValuesAreNotEqual() throws Exception {
        assertThat(ipVFutureAddress(anIpVFutureAddressVersion(), anIpVFutureAddressAddress()), not(equalTo(ipVFutureAddress(anIpVFutureAddressVersion(), anIpVFutureAddressAddress()))));
    }

    private static String anIpVFutureAddressVersion() {
        return random(5, HEX_DIGIT);
    }

    private static String anIpVFutureAddressAddress() {
        return random(5, IP_V_FUTURE_ADDRESS_CHARACTERS);
    }

    @Test
    public void ipVFutureAddressProducesCorrectToString() throws Exception {
        String versionNumber = anIpVFutureAddressVersion();
        String address = anIpVFutureAddressAddress();
        assertThat(ipVFutureAddress(versionNumber, address).toString(), equalTo("Host{version='" + versionNumber + "', address='" + address.toLowerCase() + "'}"));
    }

    @Test
    public void rejectsNullInFactoryForAnIpVFutureAddress() throws Exception {
        assertThrowsNullPointerException("Null version should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipVFutureAddress(null, anIpVFutureAddressAddress());
            }
        });
        assertThrowsNullPointerException("Null address should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                ipVFutureAddress(anIpVFutureAddressVersion(), null);
            }
        });
    }

    private static String aValidIpVFutureAddress() {
        return LOWER_CASE_ALPHA + DIGIT + "-._~" + SUB_DELIMS + ":";
    }

    private static String aValidIpVFutureVersion() {
        return HEX_DIGIT;
    }

}
