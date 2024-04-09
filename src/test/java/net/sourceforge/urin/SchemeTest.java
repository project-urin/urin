/*
 * Copyright 2024 Mark Slater
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
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.PortBuilder.aPortDifferentTo;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SchemeBuilder.aSchemeWithDefaultPort;
import static net.sourceforge.urin.SchemeBuilder.aSchemeWithNoDefaultPort;
import static net.sourceforge.urin.SchemeBuilder.aValidSchemeName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SchemeTest {
    @Test
    void lowerCaseSchemesAreUnmolested() {
        assertThat(scheme("a").asString(), equalTo("a"));
    }

    @Test
    void schemesAreLowerCased() {
        assertThat(scheme("A").asString(), equalTo("a"));
    }

    @Test
    void acceptsTheFullRangeOfValidFirstCharacters() {
        for (char character : ALPHA.toCharArray()) {
            scheme(Character.toString(character));
        }
    }

    @Test
    void rejectsNullScheme() {
        assertThrows(NullPointerException.class, () -> scheme(null), "Null value should throw NullPointerException in factory");
    }

    @Test
    void rejectsZeroLengthStringScheme() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme(""));
        assertThat(illegalArgumentException.getMessage(), equalTo("Scheme must contain at least one character"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstCharPlus() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("+"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [+]"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstCharMinus() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("-"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [-]"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstCharDot() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("."));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [.]"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstChar4() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("4"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [4]"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstCharAt() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("@"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [@]"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstCharOpenSquareBracket() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("["));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [[]"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstCharBackTick() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("`"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [`]"));
    }

    @Test
    void rejectsInvalidSchemeNameFirstCharOpenBrace() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("{"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 1 must be a-z, or A-Z in scheme [{]"));
    }

    @Test
    void acceptsFullRangeOfTailCharacters() {
        scheme("a" + DIGIT + ALPHA + "+-.");
    }

    @Test
    void rejectsInvalidSchemeNameTailCharAt() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("a@"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a@]"));
    }

    @Test
    void rejectsInvalidSchemeNameTailCharOpenSquareBracket() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("a["));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a[]"));
    }

    @Test
    void rejectsInvalidSchemeNameTailCharBackTick() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("a`"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a`]"));
    }

    @Test
    void rejectsInvalidSchemeNameTailCharOpenBrace() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("a{"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a{]"));
    }

    @Test
    void rejectsInvalidSchemeNameTailCharForwardsSlash() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("a/"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a/]"));
    }

    @Test
    void rejectsInvalidSchemeNameTailCharColon() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scheme("a:"));
        assertThat(illegalArgumentException.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a:]"));
    }

    @Test
    void aSchemeWithNoPortIsEqualToASchemeWithTheSameNameAndNoPort() {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName), equalTo(scheme(schemeName)));
        assertThat(scheme(schemeName).hashCode(), equalTo(scheme(schemeName).hashCode()));
    }

    @Test
    void aSchemeWithNoPortIsNotEqualToASchemeWithADifferentNameAndNoPort() {
        assertThat(scheme(aValidSchemeName()), not(equalTo(scheme(aValidSchemeName()))));
    }

    @Test
    void aSchemeWithNoPortIsNotEqualToASchemeWithTheSameNameButWithPort() {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName), not(equalTo(scheme(schemeName, aPort()))));
    }

    @Test
    void aSchemeWithNoPortAsStringIsCorrect() {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName).asString(), equalTo(schemeName.toLowerCase(ENGLISH)));
    }

    @Test
    void aSchemeWithNoPortToStringIsCorrect() {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName).toString(), equalTo("Scheme{name='" + schemeName.toLowerCase(ENGLISH) + "'}"));
    }

    @Test
    void aSchemeWithPortIsEqualToASchemeWithTheSameNameAndSamePort() {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        assertThat(scheme(schemeName, port), equalTo(scheme(schemeName, port)));
        assertThat(scheme(schemeName, port).hashCode(), equalTo(scheme(schemeName, port).hashCode()));
    }

    @Test
    void aSchemeWithPortIsNotEqualToASchemeWithADifferentNameAndSamePort() {
        Port port = aPort();
        assertThat(scheme(aValidSchemeName(), port), not(equalTo(scheme(aValidSchemeName(), port))));
    }

    @Test
    void aSchemeWithPortIsNotEqualToASchemeWithTheSameNameAndDifferentPort() {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        assertThat(scheme(schemeName, port), not(equalTo(scheme(schemeName, aPortDifferentTo(port)))));
    }

    @Test
    void aSchemeWithPortAsStringIsCorrect() {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName, aPort()).asString(), equalTo(schemeName.toLowerCase(ENGLISH)));
    }

    @Test
    void aSchemeWithPortToStringIsCorrect() {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        assertThat(scheme(schemeName, port).toString(), equalTo("Scheme{name='" + schemeName.toLowerCase(ENGLISH) + "', defaultPort=" + port + "}"));
    }

    @Test
    void schemeWithNoPortReturnsNormalisedAuthoritiesUnmolested() {
        Authority authority = anAuthority();
        assertThat(aSchemeWithNoDefaultPort().normalise(authority), equalTo(authority));
    }

    @Test
    void schemeWithDefaultPortReturnsAuthoritiesWithADifferentPortUnmolested() {
        Port port = aPort();
        Authority authority = authority(aHost(), aPortDifferentTo(port));
        assertThat(aSchemeWithDefaultPort(port).normalise(authority), equalTo(authority));
    }

    @Test
    void schemeWithDefaultPortReturnsAuthoritiesWithThatPortWithPortRemoved() {
        Port port = aPort();
        Host host = aHost();
        assertThat(aSchemeWithDefaultPort(port).normalise(authority(host, port)), equalTo(authority(host)));
    }

    @Test
    void rejectsNullPort() {
        assertThrows(NullPointerException.class, () -> scheme(aValidSchemeName(), null), "Null value should throw NullPointerException in factory");
    }

    @Test
    void schemeWithNoPortReturnsItselfWhenDefaultPortRemoved() {
        Scheme<?, ?, ?> scheme = aSchemeWithNoDefaultPort();
        assertThat(scheme.removeDefaultPort(), equalTo(scheme));
    }

    @Test
    void schemeWithDefaultPortCorrectlyRemovesDefaultPort() {
        String schemeName = aValidSchemeName();
        assertThat(scheme(schemeName, aPort()).removeDefaultPort(), equalTo(scheme(schemeName)));
    }

}
