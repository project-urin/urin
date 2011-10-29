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
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PercentEncodedStringValueTest {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(UNRESERVED);

    @Test
    public void twoSingleEncodedValuesOfTheSameClassWithTheSameValueAreEqual() throws Exception {
        String aString = randomAlphanumeric(5);
        assertThat(testSingleEncodedValue(aString), equalTo(testSingleEncodedValue(aString)));
        assertThat(testSingleEncodedValue(aString).hashCode(), equalTo(testSingleEncodedValue(aString).hashCode()));
    }

    @Test
    public void twoSingleEncodedValuesOfTheSameClassWithDifferentValuesAreNotEqual() throws Exception {
        assertThat(testSingleEncodedValue(randomAlphanumeric(5)), not(equalTo(testSingleEncodedValue(randomAlphanumeric(5)))));
    }

    @Test
    public void twoSingleEncodedValuesOfDifferentClassesWithTheSameValueAreEqual() throws Exception {
        String aString = randomAlphanumeric(5);
        PercentEncodedStringValue expected = new PercentEncodedStringValue(aString, PERCENT_ENCODER) {
        };
        assertThat(testSingleEncodedValue(aString), not(equalTo(expected)));
    }

    @Test
    public void toStringFormatIsCorrect() throws Exception {
        String aString = randomAlphanumeric(5);
        assertThat(testSingleEncodedValue(aString).toString(), equalTo("TestPercentEncodedStringValue{value='" + aString + "'}"));
    }

    static PercentEncodedStringValue testSingleEncodedValue(final String content) {
        return new TestPercentEncodedStringValue(content);
    }

    private static class TestPercentEncodedStringValue extends PercentEncodedStringValue {
        private TestPercentEncodedStringValue(final String content) {
            super(content, PERCENT_ENCODER);
        }
    }
}
