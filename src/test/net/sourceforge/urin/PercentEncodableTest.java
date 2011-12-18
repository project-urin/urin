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
import static net.sourceforge.urin.MoreRandomStringUtils.*;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.PercentEncodable.*;
import static net.sourceforge.urin.PercentEncodableBuilder.aPercentEncodableString;
import static net.sourceforge.urin.PercentEncoder.ENCODE_EVERYTHING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PercentEncodableTest {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(UNRESERVED);

    @Test
    public void twoPercentEncodableStringValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        String aString = aString();
        assertThat(percentEncodableString(aString), equalTo(percentEncodableString(aString)));
        assertThat(percentEncodableString(aString).hashCode(), equalTo(percentEncodableString(aString).hashCode()));
    }

    @Test
    public void twoPercentEncodableStringValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        String aString = aString();
        assertThat(percentEncodableString(aString), not(equalTo(percentEncodableString(MoreRandomStringUtils.randomDifferentTo(aString, 5)))));
    }

    @Test
    public void percentEncodableStringToStringFormatIsCorrect() throws Exception {
        String aString = aString();
        assertThat(percentEncodableString(aString).toString(), equalTo("PercentEncodable{value='" + aString + "'}"));
    }

    @Test
    public void encodesPercentEncodableStringValueCorrectly() throws Exception {
        String aString = aString();
        assertThat(percentEncodableString(aString).encode(PERCENT_ENCODER), equalTo(PERCENT_ENCODER.encode(aString)));
    }

    @Test
    public void percentEncodableStringIdentifiesEmptinessCorrectly() throws Exception {
        assertThat(percentEncodableString(aString()).isEmpty(), equalTo(false));
        assertThat(percentEncodableString("").isEmpty(), equalTo(true));
    }

    @Test
    public void percentEncodableStringIdentifiesColonCorrectly() throws Exception {
        assertThat(percentEncodableString(randomIncluding(':', 5)).containsColon(), equalTo(true));
        assertThat(percentEncodableString(randomExcluding(':', 5)).containsColon(), equalTo(false));
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
        PercentEncodable firstPercentEncodable = aPercentEncodableString();
        PercentEncodable secondPercentEncodable = aPercentEncodableString();
        assertThat(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable), equalTo(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable)));
        assertThat(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).hashCode(), equalTo(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).hashCode()));
    }

    @Test
    public void twoPercentEncodableDelimitedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        assertThat(percentEncodableDelimitedValue('a', aPercentEncodableString(), aPercentEncodableString()), not(equalTo(percentEncodableDelimitedValue('a', aPercentEncodableString(), aPercentEncodableString()))));
    }

    @Test
    public void percentEncodableDelimitedValueToStringFormatIsCorrect() throws Exception {
        char delimiter = 'a';
        PercentEncodable firstPercentEncodable = aPercentEncodableString();
        PercentEncodable secondPercentEncodable = aPercentEncodableString();
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
        assertThat(percentEncodableDelimitedValue(delimiter, aPercentEncodableString(), aPercentEncodableString()).isEmpty(), equalTo(false));
        assertThat(percentEncodableDelimitedValue(delimiter).isEmpty(), equalTo(true));
    }

    @Test
    public void percentEncodableDelimitedValueIdentifiesColonCorrectly() throws Exception {
        char delimiter = 'a';
        assertThat(percentEncodableDelimitedValue(delimiter, percentEncodableString(randomIncluding(':', 5)), aPercentEncodableString()).containsColon(), equalTo(true));
        assertThat(percentEncodableDelimitedValue(delimiter, percentEncodableString(randomExcluding(':', 5)), aPercentEncodableString()).containsColon(), equalTo(false));
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
        PercentEncodable firstPercentEncodable = aPercentEncodableString();
        PercentEncodable secondPercentEncodable = aPercentEncodableString();
        PercentEncodable[] values = new PercentEncodable[]{firstPercentEncodable, secondPercentEncodable};
        PercentEncodable percentEncodable = percentEncodableDelimitedValue(delimiter, values);
        values[0] = aPercentEncodableString();
        assertThat(percentEncodable, equalTo(percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable)));
    }

    @Test
    public void twoPercentEncodableSubstitutedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        char originalCharacter = 'a';
        char replacementCharacter = 'b';
        String value = aString();
        assertThat(percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value), equalTo(percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value)));
        assertThat(percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value).hashCode(), equalTo(percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value).hashCode()));
    }

    @Test
    public void twoPercentEncodableSubstitutedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        assertThat(percentEncodableSubstitutedValue('a', 'b', aString()), not(equalTo(percentEncodableSubstitutedValue('a', 'b', aString()))));
    }

    @Test
    public void percentEncodableSubstitutedValueToStringFormatIsCorrect() throws Exception {
        char originalCharacter = 'a';
        char replacementCharacter = 'b';
        String value = aString();
        assertThat(percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value).toString(), equalTo("PercentEncodable{originalCharacter=" + originalCharacter + ", replacementCharacter=" + replacementCharacter + ", value='" + value + "'}"));
    }

    @Test
    public void encodesPercentEncodableSubstitutedValueCorrectly() throws Exception {
        assertThat(percentEncodableSubstitutedValue(' ', '+', ". .+.").encode(PERCENT_ENCODER), equalTo(".+.%2B."));
    }

    @Test
    public void percentEncodableSubstitutedValueIdentifiesEmptinessCorrectly() throws Exception {
        assertThat(percentEncodableSubstitutedValue('a', 'A', aString()).isEmpty(), equalTo(false));
        assertThat(percentEncodableSubstitutedValue('a', 'A', "").isEmpty(), equalTo(true));
    }

    @Test
    public void percentEncodableSubstitutedValueIdentifiesColonCorrectly() throws Exception {
        assertThat(percentEncodableSubstitutedValue(':', 'A', randomIncluding(':', 5)).containsColon(), equalTo(true));
        assertThat(percentEncodableSubstitutedValue(':', 'A', randomExcluding(':', 5)).containsColon(), equalTo(false));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableSubstitutedValue() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                percentEncodableSubstitutedValue('a', 'A', null);
            }
        });
    }

    @Test
    public void encodesPercentEncodableSpecifiedValuesCorrectly() throws Exception {
        String aString = aString();
        assertThat(percentEncodableSpecifiedValue(aString, percentEncodableString(aString)).encode(PERCENT_ENCODER), equalTo(percentEncodableString(aString).encode(ENCODE_EVERYTHING)));
        assertThat(percentEncodableSpecifiedValue(randomDifferentTo(aString, 5), percentEncodableString(aString)).encode(PERCENT_ENCODER), equalTo(percentEncodableString(aString).encode(PERCENT_ENCODER)));
    }

    @Test
    public void twoPercentEncodableSpecifiedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        String aString = aString();
        PercentEncodable aPercentEncodable = aPercentEncodableString();
        assertThat(percentEncodableSpecifiedValue(aString, aPercentEncodable), equalTo(percentEncodableSpecifiedValue(aString, aPercentEncodable)));
        assertThat(percentEncodableSpecifiedValue(aString, aPercentEncodable).hashCode(), equalTo(percentEncodableSpecifiedValue(aString, aPercentEncodable).hashCode()));
    }

    @Test
    public void twoPercentEncodableSpecifiedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        String aString = aString();
        assertThat(percentEncodableSpecifiedValue(aString, aPercentEncodableString()), not(equalTo(percentEncodableSpecifiedValue(aString, aPercentEncodableString()))));
    }

    @Test
    public void twoPercentEncodableSpecifiedValuesOfTheSameClassWithDifferentSpecifiedEncodablesAreNotEqual() throws Exception {
        PercentEncodable aPercentEncodable = aPercentEncodableString();
        assertThat(percentEncodableSpecifiedValue(aString(), aPercentEncodable), not(equalTo(percentEncodableSpecifiedValue(aString(), aPercentEncodable))));
    }

    @Test
    public void percentEncodableSpecifiedValueToStringFormatIsCorrect() throws Exception {
        String aString = aString();
        PercentEncodable aPercentEncodable = aPercentEncodableString();
        assertThat(percentEncodableSpecifiedValue(aString, aPercentEncodable).toString(), equalTo("PercentEncodable{encodedValue='" + aString + "', value=" + aPercentEncodable + "}"));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableSpecifiedValue() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                percentEncodableSpecifiedValue(aString(), null);
            }
        });
        assertThrowsNullPointerException("Null specified value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                percentEncodableSpecifiedValue(null, aPercentEncodableString());
            }
        });
    }

}
