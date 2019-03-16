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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static net.sourceforge.urin.CharacterSetMembershipFunction.*;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PercentEncoderTest {

    private static final PercentEncoder NON_UNRESERVED_PERCENT_ENCODER = new PercentEncoder(UNRESERVED);

    @Test
    public void canEncodeRfc3986Examples() throws Exception {
        assertThat(NON_UNRESERVED_PERCENT_ENCODER.encode("A"), equalTo("A"));
        assertThat(NON_UNRESERVED_PERCENT_ENCODER.encode("\u00C0"), equalTo("%C3%80"));
        assertThat(NON_UNRESERVED_PERCENT_ENCODER.encode("\u30A2"), equalTo("%E3%82%A2"));
    }

    @Test
    public void handlesControlCharacters() throws Exception {
        assertThat(NON_UNRESERVED_PERCENT_ENCODER.encode("\t"), equalTo("%09"));
    }

    @Test
    public void canEncodeAdditionalCharacters() throws Exception {
        PercentEncoder aPercentEncoder = new PercentEncoder(or(singleMemberCharacterSet('B'), singleMemberCharacterSet('C')));
        assertThat(aPercentEncoder.encode("ABC"), equalTo("%41BC"));
        assertThat(aPercentEncoder.additionallyEncoding('B').encode("ABC"), equalTo("%41%42C"));
    }

    @Test
    public void emptyStringIsDecodedToEmptyString() throws Exception {
        MatcherAssert.assertThat(new PercentEncoder(NO_CHARACTERS).decode(""), Matchers.equalTo(""));
    }

    @Test
    public void unencodedStringIsDecodedToItself() throws Exception {
        String string = aString();
        MatcherAssert.assertThat(new PercentEncoder(ALL_CHARACTERS).decode(string), Matchers.equalTo(string));
    }

    @Test
    public void singleByteEncodedStringIsDecodedCorrectly() throws Exception {
        MatcherAssert.assertThat(
                new PercentEncoder(ALL_CHARACTERS).decode(
                        "%20"
                ), Matchers.equalTo(" "));
    }

    @Test
    public void repeatedSingleByteEncodedStringIsDecodedCorrectly() throws Exception {
        MatcherAssert.assertThat(
                new PercentEncoder(ALL_CHARACTERS).decode(
                        "%20%20"
                ), Matchers.equalTo("  "));
    }

    @Test
    public void encodedStringIsDecodedCorrectly() throws Exception {
        String string = aString();
        MatcherAssert.assertThat(
                new PercentEncoder(NO_CHARACTERS).decode(
                        new PercentEncoder(NO_CHARACTERS).encode(string)
                ), Matchers.equalTo(string));
    }

}
