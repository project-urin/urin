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

import static net.sourceforge.urin.CharacterSets.ALPHA;
import static net.sourceforge.urin.CharacterSets.DIGIT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SchemeTest {
    @Test
    public void lowerCaseSchemesAreUnmolested() throws Exception {
        assertThat(Scheme.scheme("a").asString(), equalTo("a"));
    }

    @Test
    public void schemesAreLowerCased() throws Exception {
        assertThat(Scheme.scheme("A").asString(), equalTo("a"));
    }

    @Test
    public void acceptsTheFullRangeOfValidFirstCharacters() throws Exception {
        for (char character : ALPHA.toCharArray()) {
            Scheme.scheme(Character.toString(character));
        }
    }

    @Test
    public void rejectsNullScheme() throws Exception {
        try {
            //noinspection NullableProblems
            Scheme.scheme(null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expect to end up here
        }
    }

    @Test
    public void rejectsZeroLengthStringScheme() throws Exception {
        try {
            Scheme.scheme("");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Scheme must contain at least one character"));
        }
    }

    @Test
    public void rejectsInvalidSchemeNameFirstChar() throws Exception {
        try {
            Scheme.scheme("+");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [+]"));
        }
        try {
            Scheme.scheme("-");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [-]"));
        }
        try {
            Scheme.scheme(".");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [.]"));
        }
        try {
            Scheme.scheme("4");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [4]"));
        }
        try {
            Scheme.scheme("@");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [@]"));
        }
        try {
            Scheme.scheme("[");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [[]"));
        }
        try {
            Scheme.scheme("`");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [`]"));
        }
        try {
            Scheme.scheme("{");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [{]"));
        }
    }

    @Test
    public void acceptsFullRangeOfTailCharacters() throws Exception {
        Scheme.scheme("a" + DIGIT + ALPHA + "+-.");
    }

    @Test
    public void rejectsInvalidSchemeNameTailChar() throws Exception {
        try {
            Scheme.scheme("a@");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a@]"));
        }
        try {
            Scheme.scheme("a[");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a[]"));
        }
        try {
            Scheme.scheme("a`");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a`]"));
        }
        try {
            Scheme.scheme("a{");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a{]"));
        }
        try {
            Scheme.scheme("a/");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a/]"));
        }
        try {
            Scheme.scheme("a:");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a:]"));
        }
    }
}
