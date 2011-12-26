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
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringDifferentTo;
import static net.sourceforge.urin.PercentEncodable.percentEncodableString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PercentEncodedStringValueTest {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(UNRESERVED);

    @Test
    public void twoSingleEncodedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        String aString = aString();
        assertThat(testSingleEncodedValue(percentEncodableString(aString)), equalTo(testSingleEncodedValue(percentEncodableString(aString))));
        assertThat(testSingleEncodedValue(percentEncodableString(aString)).hashCode(), equalTo(testSingleEncodedValue(percentEncodableString(aString)).hashCode()));
    }

    @Test
    public void twoSingleEncodedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        String aString = aString();
        assertThat(testSingleEncodedValue(percentEncodableString(aString)), not(equalTo(testSingleEncodedValue(percentEncodableString(aStringDifferentTo(aString))))));
    }

    @Test
    public void twoSingleEncodedValuesOfDifferentClassesWithTheSameValueAreNotEqual() throws Exception {
        String aString = aString();
        PercentEncodedUnaryValue expected = new PercentEncodedUnaryValue(percentEncodableString(aString), PERCENT_ENCODER) {
        };
        assertThat(testSingleEncodedValue(percentEncodableString(aString)), not(equalTo(expected)));
    }

    @Test
    public void toStringFormatIsCorrect() throws Exception {
        PercentEncodable value = percentEncodableString(aString());
        assertThat(testSingleEncodedValue(value).toString(), equalTo("TestPercentEncodedUnaryValue{value='" + value + "'}"));
    }

    static PercentEncodedUnaryValue testSingleEncodedValue(final PercentEncodable value) {
        return new TestPercentEncodedUnaryValue(value);
    }

    private static class TestPercentEncodedUnaryValue extends PercentEncodedUnaryValue {
        private TestPercentEncodedUnaryValue(final PercentEncodable value) {
            super(value, PERCENT_ENCODER);
        }
    }

}
