/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.jupiter.api.Test;

import static java.util.Locale.ENGLISH;
import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.CharacterSets.ALPHA;
import static net.sourceforge.urin.CharacterSets.DIGIT;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.PortBuilder.aPortDifferentTo;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SchemeBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Scheme.scheme(null);
            }
        });
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

    @Test
    public void aSchemeWithNoPortIsEqualToASchemeWithTheSameNameAndNoPort() throws Exception {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName), equalTo(scheme(schemeName)));
        assertThat(scheme(schemeName).hashCode(), equalTo(scheme(schemeName).hashCode()));
    }

    @Test
    public void aSchemeWithNoPortIsNotEqualToASchemeWithADifferentNameAndNoPort() throws Exception {
        assertThat(scheme(aValidSchemeName()), not(equalTo(scheme(aValidSchemeName()))));
    }

    @Test
    public void aSchemeWithNoPortIsNotEqualToASchemeWithTheSameNameButWithPort() throws Exception {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName), not(equalTo(scheme(schemeName, aPort()))));
    }

    @Test
    public void aSchemeWithNoPortAsStringIsCorrect() throws Exception {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName).asString(), equalTo(schemeName.toLowerCase(ENGLISH)));
    }

    @Test
    public void aSchemeWithNoPortToStringIsCorrect() throws Exception {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName).toString(), equalTo("Scheme{name='" + schemeName.toLowerCase(ENGLISH) + "'}"));
    }

    @Test
    public void aSchemeWithPortIsEqualToASchemeWithTheSameNameAndSamePort() throws Exception {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        assertThat(scheme(schemeName, port), equalTo(scheme(schemeName, port)));
        assertThat(scheme(schemeName, port).hashCode(), equalTo(scheme(schemeName, port).hashCode()));
    }

    @Test
    public void aSchemeWithPortIsNotEqualToASchemeWithADifferentNameAndSamePort() throws Exception {
        Port port = aPort();
        assertThat(scheme(aValidSchemeName(), port), not(equalTo(scheme(aValidSchemeName(), port))));
    }

    @Test
    public void aSchemeWithPortIsNotEqualToASchemeWithTheSameNameAndDifferentPort() throws Exception {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        assertThat(scheme(schemeName, port), not(equalTo(scheme(schemeName, aPortDifferentTo(port)))));
    }

    @Test
    public void aSchemeWithPortAsStringIsCorrect() throws Exception {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName, aPort()).asString(), equalTo(schemeName.toLowerCase(ENGLISH)));
    }

    @Test
    public void aSchemeWithPortToStringIsCorrect() throws Exception {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        assertThat(scheme(schemeName, port).toString(), equalTo("Scheme{name='" + schemeName.toLowerCase(ENGLISH) + "', defaultPort=" + port.toString() + "}"));
    }

    @Test
    public void schemeWithNoPortReturnsNormalisedAuthoritiesUnmolested() throws Exception {
        Authority authority = anAuthority();
        assertThat(aSchemeWithNoDefaultPort().normalise(authority), equalTo(authority));
    }

    @Test
    public void schemeWithDefaultPortReturnsAuthoritiesWithADifferentPortUnmolested() throws Exception {
        Port port = aPort();
        Authority authority = authority(aHost(), aPortDifferentTo(port));
        assertThat(aSchemeWithDefaultPort(port).normalise(authority), equalTo(authority));
    }

    @Test
    public void schemeWithDefaultPortReturnsAuthoritiesWithThatPortWithPortRemoved() throws Exception {
        Port port = aPort();
        Host host = aHost();
        assertThat(aSchemeWithDefaultPort(port).normalise(authority(host, port)), equalTo(authority(host)));
    }

    @Test
    public void rejectsNullPort() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                scheme(aValidSchemeName(), null);
            }
        });
    }

    @Test
    public void schemeWithNoPortReturnsItselfWhenDefaultPortRemoved() throws Exception {
        Scheme scheme = aSchemeWithNoDefaultPort();
        assertThat(scheme.removeDefaultPort(), equalTo(scheme));
    }

    @Test
    public void schemeWithDefaultPortCorrectlyRemovesDefaultPort() throws Exception {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName, aPort()).removeDefaultPort(), equalTo(scheme(schemeName)));
    }

}
