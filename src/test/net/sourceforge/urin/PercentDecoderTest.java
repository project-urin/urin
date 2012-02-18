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

import static net.sourceforge.urin.CharacterSetMembershipFunction.ALL_CHARACTERS;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PercentDecoderTest {

    @Test
    public void emptyStringIsDecodedToEmptyString() throws Exception {
        assertThat(new PercentDecoder(NO_CHARACTERS).decode(""), equalTo(""));
    }

    @Test
    public void unencodedStringIsDecodedToItself() throws Exception {
        String string = aString();
        assertThat(new PercentDecoder(ALL_CHARACTERS).decode(string), equalTo(string));
    }

    @Test
    public void singleByteEncodedStringIsDecodedCorrectly() throws Exception {
        assertThat(
                new PercentDecoder(ALL_CHARACTERS).decode(
                        "%20"
                ), equalTo(" "));
    }

    @Test
    public void repeatedSingleByteEncodedStringIsDecodedCorrectly() throws Exception {
        assertThat(
                new PercentDecoder(ALL_CHARACTERS).decode(
                        "%20%20"
                ), equalTo("  "));
    }

    @Test
    public void encodedStringIsDecodedCorrectly() throws Exception {
        String string = aString();
        assertThat(
                new PercentDecoder(NO_CHARACTERS).decode(
                        new PercentEncoder(NO_CHARACTERS).encode(string)
                ), equalTo(string));
    }

}