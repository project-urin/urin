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

import static net.sourceforge.urin.CharacterSets.P_CHARS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class NonEmptySegmentTest {
    @Test
    public void nonEmptySegmentRejectsEmptyString() throws Exception {
        try {
            new NonEmptySegment("");
            fail("Should have thrown illegal argument exception");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Must contain at least one character"));
        }
    }

    @Test
    public void asStringReturnsValueProvidedForUnreservedCharacters() throws Exception {
        assertThat(new NonEmptySegment(P_CHARS).asString(), equalTo(P_CHARS));
    }

    @Test
    public void asStringPercentEncodesNonUnreservedCharacters() throws Exception {
        assertThat(new NonEmptySegment(".#.[.]. .").asString(), equalTo(".%23.%5B.%5D.%20."));
    }


}
