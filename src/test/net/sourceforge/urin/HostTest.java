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

import static net.sourceforge.urin.CharacterSets.SUB_DELIMS;
import static net.sourceforge.urin.CharacterSets.UNRESERVED;
import static net.sourceforge.urin.DecimalOctetBuilder.aDecimalOctet;
import static net.sourceforge.urin.Host.ipV4Address;
import static net.sourceforge.urin.Host.registeredName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HostTest {
    @Test
    public void registeredNameAsStringReturnsValueProvidedForUnreservedCharacters() throws Exception {
        String nonPercentEncodedCharacters = UNRESERVED + SUB_DELIMS;
        assertThat(registeredName(nonPercentEncodedCharacters).asString(), equalTo(nonPercentEncodedCharacters));
    }

    @Test
    public void registeredNameAsStringPercentEncodesNonUnreservedCharacters() throws Exception {
        assertThat(registeredName(".:.@.#.[.]. .").asString(), equalTo(".%3A.%40.%23.%5B.%5D.%20."));
    }

    @Test
    public void ipV4AddressAsStringIsCorrect() throws Exception {
        DecimalOctet firstDecimalOctet = aDecimalOctet();
        DecimalOctet secondDecimalOctet = aDecimalOctet();
        DecimalOctet thirdDecimalOctet = aDecimalOctet();
        DecimalOctet fourthDecimalOctet = aDecimalOctet();
        assertThat(
                ipV4Address(firstDecimalOctet, secondDecimalOctet, thirdDecimalOctet, fourthDecimalOctet).asString(),
                equalTo(firstDecimalOctet.asString() + "." + secondDecimalOctet.asString() + "." + thirdDecimalOctet.asString() + "." + fourthDecimalOctet.asString()));
    }

}