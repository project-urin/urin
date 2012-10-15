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

import java.util.Collections;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;
import static net.sourceforge.urin.CharacterSetMembershipFunction.UNRESERVED;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.MoreRandomStringUtils.*;
import static net.sourceforge.urin.PercentEncoder.ENCODE_EVERYTHING;
import static net.sourceforge.urin.PercentEncoder.ENCODE_NOTHING;
import static net.sourceforge.urin.PercentEncodingUnaryValue.PercentEncoding.percentEncodingString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PercentEncodingTest {

    private static final PercentEncoder RESERVED_PERCENT_ENCODER = new PercentEncoder(UNRESERVED);
    private static final PercentEncoder EVERYTHING_PERCENT_ENCODER = new PercentEncoder(NO_CHARACTERS);

    @Test
    public void encodesPercentEncodableStringValueCorrectly() throws Exception {
        String aString = aString();
        assertThat(percentEncodingString(RESERVED_PERCENT_ENCODER).encode(aString), equalTo(RESERVED_PERCENT_ENCODER.encode(aString)));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableString() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodingUnaryValue.PercentEncoding.percentEncodingString(null);
            }
        });
    }

    @Test
    public void encodesPercentEncodableDelimitedValueWithNoSubDelimitersCorrectly() throws Exception {
        assertThat(PercentEncodingUnaryValue.PercentEncoding.percentEncodingDelimitedValue('&', percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(asList(CharacterSets.UNRESERVED, CharacterSets.UNRESERVED)), equalTo(CharacterSets.UNRESERVED + "&" + CharacterSets.UNRESERVED));
    }

    @Test
    public void encodesPercentEncodableDelimitedValueWithSubDelimitersCorrectly() throws Exception {
        assertThat(PercentEncodingUnaryValue.PercentEncoding.percentEncodingDelimitedValue('&', percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(asList(". ./.&.")), equalTo(".%20.%2F.%26."));
    }

    @Test
    public void percentEncodableDelimitedValueIdentifiesEmptinessCorrectly() throws Exception {
        assertThat(PercentEncodingUnaryValue.PercentEncoding.percentEncodingDelimitedValue(aChar(), percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(asList(CharacterSets.UNRESERVED, CharacterSets.UNRESERVED)).isEmpty(), equalTo(false));
        assertThat(PercentEncodingUnaryValue.PercentEncoding.percentEncodingDelimitedValue(aChar(), percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(Collections.<String>emptyList()).isEmpty(), equalTo(true));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableDelimitedValue() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                final PercentEncodingUnaryValue.PercentEncoding<Object> percentEncoding = null;
                PercentEncodingUnaryValue.PercentEncoding.percentEncodingDelimitedValue(aChar(), percentEncoding);
            }
        });
    }

    @Test
    public void encodesPercentEncodableSubstitutedValueCorrectly() throws Exception {
        assertThat(PercentEncodingUnaryValue.PercentEncoding.percentEncodingSubstitutedValue(' ', '+', percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(". .+."), equalTo(".+.%2B."));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableSubstitutedValue() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodingUnaryValue.PercentEncoding.percentEncodingSubstitutedValue(aChar(), aChar(), null);
            }
        });
    }

    @Test
    public void encodesPercentEncodableSpecifiedValuesCorrectly() throws Exception {
        String aString = aString();
        assertThat(PercentEncodingUnaryValue.PercentEncoding.specifiedValueEncoding(aString, percentEncodingString(ENCODE_NOTHING)).encode(aString), equalTo(ENCODE_EVERYTHING.encode(aString)));
        PercentEncodingUnaryValue.PercentEncoding<String> percentEncoding = percentEncodingString(ENCODE_NOTHING);
        assertThat(PercentEncodingUnaryValue.PercentEncoding.specifiedValueEncoding(aStringDifferentTo(aString), percentEncoding).encode(aString), equalTo(percentEncoding.encode(aString)));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableSpecifiedValue() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodingUnaryValue.PercentEncoding.specifiedValueEncoding(null, percentEncodingString(EVERYTHING_PERCENT_ENCODER));
            }
        });
        assertThrowsException("Null specified value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodingUnaryValue.PercentEncoding.specifiedValueEncoding(aString(), null);
            }
        });
    }

}
