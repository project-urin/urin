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

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;
import static net.sourceforge.urin.CharacterSetMembershipFunction.UNRESERVED;
import static net.sourceforge.urin.MoreRandomStringUtils.*;
import static net.sourceforge.urin.PercentEncoder.ENCODE_EVERYTHING;
import static net.sourceforge.urin.PercentEncoder.ENCODE_NOTHING;
import static net.sourceforge.urin.PercentEncodingPartial.PercentEncoding.percentEncodingString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PercentEncodingTest {

    private static final PercentEncoder RESERVED_PERCENT_ENCODER = new PercentEncoder(UNRESERVED);
    private static final PercentEncoder EVERYTHING_PERCENT_ENCODER = new PercentEncoder(NO_CHARACTERS);

    @Test
    void encodesPercentEncodableStringValueCorrectly() {
        String aString = aString();
        assertThat(percentEncodingString(RESERVED_PERCENT_ENCODER).encode(aString), equalTo(RESERVED_PERCENT_ENCODER.encode(aString)));
    }

    @Test
    void rejectsNullInFactoryForPercentEncodableString() {
        assertThrows(NullPointerException.class, () -> PercentEncodingPartial.PercentEncoding.percentEncodingString(null), "Null value should throw NullPointerException in factory");
    }

    @Test
    void encodesPercentEncodableDelimitedValueWithNoSubDelimitersCorrectly() {
        assertThat(PercentEncodingPartial.PercentEncoding.percentEncodingDelimitedValue('&', percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(asList(CharacterSets.UNRESERVED, CharacterSets.UNRESERVED)), equalTo(CharacterSets.UNRESERVED + "&" + CharacterSets.UNRESERVED));
    }

    @Test
    void encodesPercentEncodableDelimitedValueWithSubDelimitersCorrectly() {
        assertThat(PercentEncodingPartial.PercentEncoding.percentEncodingDelimitedValue('&', percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(singletonList(". ./.&.")), equalTo(".%20.%2F.%26."));
    }

    @Test
    void percentEncodableDelimitedValueIdentifiesEmptinessCorrectly() {
        assertThat(PercentEncodingPartial.PercentEncoding.percentEncodingDelimitedValue(aChar(), percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(asList(CharacterSets.UNRESERVED, CharacterSets.UNRESERVED)).isEmpty(), equalTo(false));
        assertThat(PercentEncodingPartial.PercentEncoding.percentEncodingDelimitedValue(aChar(), percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(Collections.emptyList()).isEmpty(), equalTo(true));
    }

    @Test
    void rejectsNullInFactoryForPercentEncodableDelimitedValue() {
        assertThrows(NullPointerException.class, () -> {
            final PercentEncodingPartial.PercentEncoding<Object> percentEncoding = null;
            PercentEncodingPartial.PercentEncoding.percentEncodingDelimitedValue(aChar(), percentEncoding);
        }, "Null value should throw NullPointerException in factory");
    }

    @Test
    void encodesPercentEncodableSubstitutedValueCorrectly() {
        assertThat(PercentEncodingPartial.percentEncodingSubstitutedValue(' ', '+').apply(percentEncodingString(RESERVED_PERCENT_ENCODER)).encode(". .+."), equalTo(".+.%2B."));
    }

    @Test
    void encodesPercentEncodableSpecifiedValuesCorrectly() {
        String aString = aString();
        assertThat(PercentEncodingPartial.PercentEncoding.specifiedValueEncoding(aString, percentEncodingString(ENCODE_NOTHING)).encode(aString), equalTo(ENCODE_EVERYTHING.encode(aString)));
        PercentEncodingPartial.PercentEncoding<String> percentEncoding = percentEncodingString(ENCODE_NOTHING);
        assertThat(PercentEncodingPartial.PercentEncoding.specifiedValueEncoding(aStringDifferentTo(aString), percentEncoding).encode(aString), equalTo(percentEncoding.encode(aString)));
    }

    @Test
    void rejectsNullInFactoryForPercentEncodableSpecifiedValue() {
        assertThrows(NullPointerException.class, () -> PercentEncodingPartial.PercentEncoding.specifiedValueEncoding(null, percentEncodingString(EVERYTHING_PERCENT_ENCODER)), "Null value should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> PercentEncodingPartial.PercentEncoding.specifiedValueEncoding(aString(), null), "Null specified value should throw NullPointerException in factory");
    }

}
