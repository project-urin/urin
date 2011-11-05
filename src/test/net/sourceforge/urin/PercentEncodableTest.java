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

import static java.util.Arrays.asList;
import static net.sourceforge.urin.CharacterSetMembershipFunction.UNRESERVED;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.PercentEncodable.percentEncodableDelimitedValue;
import static net.sourceforge.urin.PercentEncodable.percentEncodableString;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PercentEncodableTest {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(UNRESERVED);

    @Test
    public void twoPercentEncodableStringValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        String aString = randomAlphanumeric(5);
        assertThat(percentEncodableString(aString), equalTo(percentEncodableString(aString)));
        assertThat(percentEncodableString(aString).hashCode(), equalTo(percentEncodableString(aString).hashCode()));
    }

    @Test
    public void twoPercentEncodableStringValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        assertThat(percentEncodableString(randomAlphanumeric(5)), not(equalTo(percentEncodableString(randomAlphanumeric(5)))));
    }

    @Test
    public void percentEncodableStringToStringFormatIsCorrect() throws Exception {
        String aString = randomAlphanumeric(5);
        assertThat(percentEncodableString(aString).toString(), equalTo("PercentEncodable{value='" + aString + "'}"));
    }

    @Test
    public void encodesPercentEncodableStringValueCorrectly() throws Exception {
        String aString = randomAlphanumeric(5);
        assertThat(percentEncodableString(aString).encode(PERCENT_ENCODER), equalTo(PERCENT_ENCODER.encode(aString)));
    }

    @Test
    public void percentEncodableStringIdentifiesEmptinessCorrectly() throws Exception {
        assertThat(percentEncodableString(randomAlphanumeric(5)).isEmpty(), equalTo(false));
        assertThat(percentEncodableString("").isEmpty(), equalTo(true));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableString() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                percentEncodableString(null);
            }
        });
    }

    @Test
    public void twoPercentEncodableDelimitedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        char delimiter = 'a';
        PercentEncodable firstPercentEncodable = PercentEncodableBuilder.aPercentEncodableString();
        PercentEncodable secondPercentEncodable = PercentEncodableBuilder.aPercentEncodableString();
        assertThat(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable), equalTo(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable)));
        assertThat(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).hashCode(), equalTo(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).hashCode()));
    }

    @Test
    public void twoPercentEncodableDelimitedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        assertThat(percentEncodableDelimitedValue('a', PercentEncodableBuilder.aPercentEncodableString(), PercentEncodableBuilder.aPercentEncodableString()), not(equalTo(percentEncodableDelimitedValue('a', PercentEncodableBuilder.aPercentEncodableString(), PercentEncodableBuilder.aPercentEncodableString()))));
    }

    @Test
    public void percentEncodableDelimitedValueToStringFormatIsCorrect() throws Exception {
        char delimiter = 'a';
        PercentEncodable firstPercentEncodable = PercentEncodableBuilder.aPercentEncodableString();
        PercentEncodable secondPercentEncodable = PercentEncodableBuilder.aPercentEncodableString();
        assertThat(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).toString(), equalTo("PercentEncodable{delimiter=" + delimiter + ", values=" + asList(firstPercentEncodable, secondPercentEncodable) + "}"));
    }

    @Test
    public void encodesPercentEncodableDelimitedValueWithNoSubDelimitersCorrectly() throws Exception {
        assertThat(percentEncodableDelimitedValue('&', percentEncodableString(CharacterSets.UNRESERVED), percentEncodableString(CharacterSets.UNRESERVED)).encode(PERCENT_ENCODER), equalTo(CharacterSets.UNRESERVED + "&" + CharacterSets.UNRESERVED));
    }

    @Test
    public void encodesPercentEncodableDelimitedValueWithSubDelimitersCorrectly() throws Exception {
        assertThat(percentEncodableDelimitedValue('&', percentEncodableString(". ./.&."), percentEncodableString(". ./.&.")).encode(PERCENT_ENCODER), equalTo(".%20.%2F.%26." + "&" + ".%20.%2F.%26."));
    }

    @Test
    public void percentEncodableDelimitedValueIdentifiesEmptinessCorrectly() throws Exception {
        char delimiter = 'a';
        assertThat(percentEncodableDelimitedValue(delimiter, PercentEncodableBuilder.aPercentEncodableString(), PercentEncodableBuilder.aPercentEncodableString()).isEmpty(), equalTo(false));
        assertThat(percentEncodableDelimitedValue(delimiter).isEmpty(), equalTo(true));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableDelimitedValue() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodable value = null;
                percentEncodableDelimitedValue('a', value);
            }
        });
        assertThrowsNullPointerException("Null value array should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodable[] values = null;
                percentEncodableDelimitedValue('a', values);
            }
        });
    }

    @Test
    public void percentEncodableDelimitedValueHasImmutableVarargs() throws Exception {
        char delimiter = 'a';
        PercentEncodable firstPercentEncodable = PercentEncodableBuilder.aPercentEncodableString();
        PercentEncodable secondPercentEncodable = PercentEncodableBuilder.aPercentEncodableString();
        PercentEncodable[] values = new PercentEncodable[]{firstPercentEncodable, secondPercentEncodable};
        PercentEncodable percentEncodable = percentEncodableDelimitedValue(delimiter, values);
        values[0] = PercentEncodableBuilder.aPercentEncodableString();
        assertThat(percentEncodable, equalTo(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable)));
    }
}
