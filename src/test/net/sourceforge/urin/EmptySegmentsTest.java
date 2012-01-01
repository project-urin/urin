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

import static net.sourceforge.urin.Segments.emptySegments;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class EmptySegmentsTest {
    @Test
    public void anEmptySegmentsIsEqualToAnotherEmptySegments() throws Exception {
        assertThat(emptySegments(), equalTo(emptySegments()));
        assertThat(emptySegments().hashCode(), equalTo(emptySegments().hashCode()));
    }

    @Test
    public void aSegmentsToStringIsCorrect() throws Exception {
        assertThat(emptySegments().toString(), equalTo("EmptySegments"));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(emptySegments().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartDoesNotContainColon() throws Exception {
        assertThat(emptySegments().firstPartIsSuppliedButContainsColon(), equalTo(false));
    }

}
