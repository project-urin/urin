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

import static java.util.Arrays.asList;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;
import static net.sourceforge.urin.CharacterSetMembershipFunction.UNRESERVED;
import static net.sourceforge.urin.MoreRandomStringUtils.*;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.PercentEncodableBuilder.aPercentEncodableString;
import static net.sourceforge.urin.PercentEncoder.ENCODE_EVERYTHING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PercentEncodableTest {

    private static final PercentEncoder RESERVED_PERCENT_ENCODER = new PercentEncoder(UNRESERVED);
    private static final PercentEncoder EVERYTHING_PERCENT_ENCODER = new PercentEncoder(NO_CHARACTERS);

    @Test
    public void twoPercentEncodableStringValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        String aString = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString)));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString).hashCode(), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString).hashCode()));
    }

    @Test
    public void twoPercentEncodableStringValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        String aString = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString), not(equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aStringDifferentTo(aString)))));
    }

    @Test
    public void percentEncodableStringToStringFormatIsCorrect() throws Exception {
        String aString = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString).toString(), equalTo("PercentEncodable{value='" + aString + "'}"));
    }

    @Test
    public void encodesPercentEncodableStringValueCorrectly() throws Exception {
        String aString = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString).encode(RESERVED_PERCENT_ENCODER), equalTo(RESERVED_PERCENT_ENCODER.encode(aString)));
    }

    @Test
    public void percentEncodableStringIdentifiesEmptinessCorrectly() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString()).isEmpty(), equalTo(false));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString("").isEmpty(), equalTo(true));
    }

    @Test
    public void percentEncodableStringIdentifiesColonCorrectly() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aStringIncluding(':')).containsColon(), equalTo(true));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aStringExcluding(':')).containsColon(), equalTo(false));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableString() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(null);
            }
        });
    }

    @Test
    public void twoPercentEncodableDelimitedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        char delimiter = 'a';
        PercentEncodedUnaryValue.PercentEncodable firstPercentEncodable = aPercentEncodableString();
        PercentEncodedUnaryValue.PercentEncodable secondPercentEncodable = aPercentEncodableString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable)));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).hashCode(), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).hashCode()));
    }

    @Test
    public void twoPercentEncodableDelimitedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue('a', aPercentEncodableString(), aPercentEncodableString()), not(equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue('a', aPercentEncodableString(), aPercentEncodableString()))));
    }

    @Test
    public void percentEncodableDelimitedValueToStringFormatIsCorrect() throws Exception {
        char delimiter = 'a';
        PercentEncodedUnaryValue.PercentEncodable firstPercentEncodable = aPercentEncodableString();
        PercentEncodedUnaryValue.PercentEncodable secondPercentEncodable = aPercentEncodableString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable).toString(), equalTo("PercentEncodable{delimiter=" + delimiter + ", values=" + asList(firstPercentEncodable, secondPercentEncodable) + "}"));
    }

    @Test
    public void encodesPercentEncodableDelimitedValueWithNoSubDelimitersCorrectly() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue('&', PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(CharacterSets.UNRESERVED), PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(CharacterSets.UNRESERVED)).encode(RESERVED_PERCENT_ENCODER), equalTo(CharacterSets.UNRESERVED + "&" + CharacterSets.UNRESERVED));
    }

    @Test
    public void encodesPercentEncodableDelimitedValueWithSubDelimitersCorrectly() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue('&', PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(". ./.&."), PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(". ./.&.")).encode(RESERVED_PERCENT_ENCODER), equalTo(".%20.%2F.%26." + "&" + ".%20.%2F.%26."));
    }

    @Test
    public void percentEncodableDelimitedValueIdentifiesEmptinessCorrectly() throws Exception {
        char delimiter = 'a';
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, aPercentEncodableString(), aPercentEncodableString()).isEmpty(), equalTo(false));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter).isEmpty(), equalTo(true));
    }

    @Test
    public void percentEncodableDelimitedValueIdentifiesColonCorrectly() throws Exception {
        char delimiter = 'a';
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aStringIncluding(':')), aPercentEncodableString()).containsColon(), equalTo(true));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aStringExcluding(':')), aPercentEncodableString()).containsColon(), equalTo(false));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableDelimitedValue() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodedUnaryValue.PercentEncodable value = null;
                PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue('a', value);
            }
        });
        assertThrowsNullPointerException("Null value array should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodedUnaryValue.PercentEncodable[] values = null;
                PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue('a', values);
            }
        });
    }

    @Test
    public void percentEncodableDelimitedValueHasImmutableVarargs() throws Exception {
        char delimiter = 'a';
        PercentEncodedUnaryValue.PercentEncodable firstPercentEncodable = aPercentEncodableString();
        PercentEncodedUnaryValue.PercentEncodable secondPercentEncodable = aPercentEncodableString();
        PercentEncodedUnaryValue.PercentEncodable[] values = new PercentEncodedUnaryValue.PercentEncodable[]{firstPercentEncodable, secondPercentEncodable};
        PercentEncodedUnaryValue.PercentEncodable percentEncodable = PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, values);
        values[0] = aPercentEncodableString();
        assertThat(percentEncodable, equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableDelimitedValue(delimiter, firstPercentEncodable, secondPercentEncodable)));
    }

    @Test
    public void twoPercentEncodableSubstitutedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        char originalCharacter = 'a';
        char replacementCharacter = 'b';
        String value = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value)));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value).hashCode(), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value).hashCode()));
    }

    @Test
    public void twoPercentEncodableSubstitutedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue('a', 'b', aString()), not(equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue('a', 'b', aString()))));
    }

    @Test
    public void percentEncodableSubstitutedValueToStringFormatIsCorrect() throws Exception {
        char originalCharacter = 'a';
        char replacementCharacter = 'b';
        String value = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(originalCharacter, replacementCharacter, value).toString(), equalTo("PercentEncodable{originalCharacter=" + originalCharacter + ", replacementCharacter=" + replacementCharacter + ", value='" + value + "'}"));
    }

    @Test
    public void encodesPercentEncodableSubstitutedValueCorrectly() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(' ', '+', ". .+.").encode(RESERVED_PERCENT_ENCODER), equalTo(".+.%2B."));
    }

    @Test
    public void percentEncodableSubstitutedValueIdentifiesEmptinessCorrectly() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue('a', 'A', aString()).isEmpty(), equalTo(false));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue('a', 'A', "").isEmpty(), equalTo(true));
    }

    @Test
    public void percentEncodableSubstitutedValueIdentifiesColonCorrectly() throws Exception {
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(':', 'A', aStringIncluding(':')).containsColon(), equalTo(true));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue(':', 'A', aStringExcluding(':')).containsColon(), equalTo(false));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableSubstitutedValue() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodedUnaryValue.PercentEncodable.percentEncodableSubstitutedValue('a', 'A', null);
            }
        });
    }

    @Test
    public void encodesPercentEncodableSpecifiedValuesCorrectly() throws Exception {
        String aString = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString)).encode(EVERYTHING_PERCENT_ENCODER), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString).encode(ENCODE_EVERYTHING)));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aStringDifferentTo(aString), PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString)).encode(RESERVED_PERCENT_ENCODER), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableString(aString).encode(RESERVED_PERCENT_ENCODER)));
    }

    @Test
    public void twoPercentEncodableSpecifiedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        String aString = aString();
        PercentEncodedUnaryValue.PercentEncodable aPercentEncodable = aPercentEncodableString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, aPercentEncodable), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, aPercentEncodable)));
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, aPercentEncodable).hashCode(), equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, aPercentEncodable).hashCode()));
    }

    @Test
    public void twoPercentEncodableSpecifiedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        String aString = aString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, aPercentEncodableString()), not(equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, aPercentEncodableString()))));
    }

    @Test
    public void twoPercentEncodableSpecifiedValuesOfTheSameClassWithDifferentSpecifiedEncodablesAreNotEqual() throws Exception {
        PercentEncodedUnaryValue.PercentEncodable aPercentEncodable = aPercentEncodableString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString(), aPercentEncodable), not(equalTo(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString(), aPercentEncodable))));
    }

    @Test
    public void percentEncodableSpecifiedValueToStringFormatIsCorrect() throws Exception {
        String aString = aString();
        PercentEncodedUnaryValue.PercentEncodable aPercentEncodable = aPercentEncodableString();
        assertThat(PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString, aPercentEncodable).toString(), equalTo("PercentEncodable{encodedValue='" + aString + "', value=" + aPercentEncodable + "}"));
    }

    @Test
    public void rejectsNullInFactoryForPercentEncodableSpecifiedValue() throws Exception {
        assertThrowsNullPointerException("Null value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(aString(), null);
            }
        });
        assertThrowsNullPointerException("Null specified value should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                PercentEncodedUnaryValue.PercentEncodable.percentEncodableSpecifiedValue(null, aPercentEncodableString());
            }
        });
    }

}
