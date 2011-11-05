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

import static net.sourceforge.urin.CharacterSetMembershipFunction.UNRESERVED;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
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


}
