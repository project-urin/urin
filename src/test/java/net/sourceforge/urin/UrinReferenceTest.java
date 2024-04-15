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

import java.net.URI;

import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.RelativeReferenceBuilder.anUnpollutedRelativeReference;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.UrinBuilder.anUnpollutedUrin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrinReferenceTest {

    @Test
    void aUriAsStringParsesToAUrin() throws Exception {
        Urin<String, Query<String>, Fragment<String>> urin = anUnpollutedUrin();
        assertThat(aScheme().parseUrinReference(urin.asString()), equalTo(urin));
    }

    @Test
    void aUriRoundTripsAsUrinReference() throws Exception {
        URI uriReference = URI.create("http://some.where/some/thing");
        assertThat(aScheme().parseUrinReference(uriReference).asUri(), equalTo(uriReference));
    }

    @Test
    void aRelativeReferenceAsStringParsesToARelativeReference() throws Exception {
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = anUnpollutedRelativeReference();
        assertThat(aScheme().parseUrinReference(relativeReference.asString()), equalTo(relativeReference));
    }

    @Test
    void canSetPathOnARelativeReference() throws Exception {
        final var scheme = aScheme();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> updatedPath = anAbsolutePath();
        assertThat(scheme.parseUrinReference(scheme.relativeReference(aPath(), query, fragment).asString()).withPath(updatedPath), equalTo(scheme.relativeReference(updatedPath, query, fragment)));
    }

    @Test
    void canSetPathOnAUri() throws Exception {
        final var scheme = aScheme();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> updatedPath = anAbsolutePath();
        assertThat(scheme.parseUrin(scheme.urin(aPath(), query, fragment).asString()).withPath(updatedPath), equalTo(scheme.urin(updatedPath, query, fragment)));
    }

    @Test
    void anInvalidStringThrowsAParseException() {
        assertThrows(ParseException.class, () -> aScheme().parseUrinReference("cache_object://"), "Invalid scheme should throw parse exception");
    }

    @Test
    void parsingNullThrowsNullPointerException() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrows(NullPointerException.class, () -> scheme.parseUrinReference((String) null), "Null value should throw NullPointerException in parser");
    }

    @Test
    void anEmptyStringParsesToARelativeReferenceOfEmptyPath() throws Exception {
        final UrinReference<String, Query<String>, Fragment<String>> urinReference = aScheme().parseUrinReference("");
        assertThat(urinReference.asString(), equalTo(""));
    }
}
