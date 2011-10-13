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

import static net.sourceforge.urin.CharacterSets.DIGIT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PortTest {
    @Test
    public void allowsFullRangeOfValidCharacters() throws Exception {
        new Port(DIGIT);
    }

    @Test
    public void asStringReturnsGivenPort() throws Exception {
        assertThat(new Port("123").asString(), equalTo("123"));
    }

    @Test
    public void rejectsNullPort() throws Exception {
        try {
            new Port(null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expect to end up here
        }
    }

    @Test
    public void allowsZeroLengthStringPort() throws Exception {
        new Port("");
    }

    @Test
    public void rejectsInvalidCharacters() throws Exception {
        try {
            new Port("a");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [a]"));
        }
        try {
            new Port("/");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [/]"));
        }
        try {
            new Port(":");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be 0-9 in port [:]"));
        }
    }
}
