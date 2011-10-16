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

import org.junit.Ignore;
import org.junit.Test;

import static net.sourceforge.urin.CharacterSets.*;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.Host.ipV4Address;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.OctetBuilder.anOctet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
    @Ignore
    public void ipV6AddressAsStringIsCorrect() throws Exception {
        Hexadectet firstHexadectet = hexadectet(0xFFFF);
    }

}
