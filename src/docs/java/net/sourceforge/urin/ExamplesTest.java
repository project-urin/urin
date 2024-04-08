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

import net.sourceforge.urin.scheme.http.Http;
import net.sourceforge.urin.scheme.http.HttpQuery;
import net.sourceforge.urin.scheme.http.Https;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExamplesTest {

    @Test
    void generateAnHttpsUri() {
        // tag::generateAnHttpsUri[]
        String uri = Https.https(
                Host.registeredName("www.example.com"),
                Path.path("music", "AC/DC", "Back in Black")
        ).asString();
        // end::generateAnHttpsUri[]
        assertEquals("https://www.example.com/music/AC%2FDC/Back%20in%20Black", uri);
    }

    @Test
    void parseAnHttpsUri() throws ParseException {
        // tag::parseAnHttpsUri[]
        var urin = Https.parseHttpUrin("https://www.example.com/music/AC%2FDC/Back%20in%20Black");
        List<Segment<String>> segments = urin.path().segments();
        // end::parseAnHttpsUri[]
        assertEquals(segments, asList(Segment.segment("music"), Segment.segment("AC/DC"), Segment.segment("Back in Black")));
    }

    @Test
    void generateAnFtpUri() {
        // tag::generateAnFtpUri[]
        String uri = Scheme.scheme("ftp").urin(
                Authority.authority(Host.registeredName("ftp.is.co.za")),
                Path.path("rfc", "rfc1808.txt")
        ).asString();
        // end::generateAnFtpUri[]
        final String expected = "ftp://ftp.is.co.za/rfc/rfc1808.txt";
        assertEquals(expected, uri);
    }

    @Test
    void generateAMailtoUri() throws URISyntaxException {
        // tag::generateAMailtoUri[]
        URI uri = Scheme.scheme("mailto").urin(
                Path.rootlessPath("John.Doe@example.com")
        ).asUri();
        // end::generateAMailtoUri[]
        final URI expected = new URI("mailto:John.Doe@example.com");
        assertEquals(expected, uri);
    }

    @Test
    void generateAnRsyncRelativeReference() {
        // tag::generateAnRsyncRelativeReference[]
        String uri = Scheme.scheme("rsync").relativeReference(
                Path.rootlessPath(Segment.dotDot(), Segment.segment("sibling")),
                Query.query("some-query")
        ).asString();
        // end::generateAnRsyncRelativeReference[]
        final String expected = "../sibling?some-query";
        assertEquals(expected, uri);
    }

    @Test
    void generateAFullFeaturedHttpUri() {
        // tag::generateAFullFeaturedHttpUri[]
        String uri = Http.http(
                Host.registeredName("www.example.com"),
                Port.port(80),
                Path.path("music", "AC/DC", "Back in Black"),
                HttpQuery.queryParameters(
                        HttpQuery.queryParameter("track", "Hells Bells"),
                        HttpQuery.queryParameter("version", "Radio edit")
                ),
                Fragment.fragment("verse 2")
        ).asString();
        // end::generateAFullFeaturedHttpUri[]
        final String expected = "http://www.example.com/music/AC%2FDC/Back%20in%20Black?track=Hells+Bells&version=Radio+edit#verse%202";
        assertEquals(expected, uri);
    }

    @Test
    @SuppressWarnings("unused")
    void parseAnLdapUri() {
        // tag::parseAnLdapUri[]
        try {
            var parsedUrin = Scheme.scheme("ldap").parseUrin("ldap://[2001:db8::7]/c=GB?objectClass?one");
        } catch (ParseException e) {
            // handle parse failure
        }
        // end::parseAnLdapUri[]
    }

    @Test
    void parseANonNormalisedUrin() throws ParseException {
        // tag::parseANonNormalisedUrin[]
        String uri = Http.parseHttpUrin("HTTP://www.example.com/.././some%20pat%68").asString();
        // end::parseANonNormalisedUrin[]
        final String expected = "http://www.example.com/some%20path";
        assertEquals(expected, uri);
    }

    @Test
    void resolveARelativeReference() {
        // tag::resolveARelativeReference[]
        var relativeReference = Http.HTTP.relativeReference(
                Path.rootlessPath(Segment.dotDot(), Segment.segment("child-2")),
                HttpQuery.queryParameters(HttpQuery.queryParameter("extra-query"))
        );

        String uri = Http.http(
                Authority.authority(Host.registeredName("www.example.com")),
                Path.path("child-1")
        ).resolve(
                relativeReference
        ).asString();
        // end::resolveARelativeReference[]
        final String expected = "http://www.example.com/child-2?extra-query";
        assertEquals(expected, uri);
    }
}
