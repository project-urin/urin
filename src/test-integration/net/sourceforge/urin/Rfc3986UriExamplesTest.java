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

import java.net.URI;
import java.net.URISyntaxException;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.Octet.octet;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986UriExamplesTest {

    @Test
    public void ftpExample() throws Exception {
        assertAsStringAsUriAndParse("ftp://ftp.is.co.za/rfc/rfc1808.txt", urin(
                scheme("ftp"),
                hierarchicalPart(
                        authority(registeredName("ftp.is.co.za")),
                        segments(segment("rfc"), segment("rfc1808.txt")))
        ));
    }

    @Test
    public void httpExample() throws Exception {
        assertAsStringAsUriAndParse("http://www.ietf.org/rfc/rfc2396.txt", urin(
                scheme("http"),
                hierarchicalPart(
                        authority(registeredName("www.ietf.org")),
                        segments(segment("rfc"), segment("rfc2396.txt")))
        ));
    }

    @Test
    public void ldapExample() throws Exception {
        assertAsStringAsUriAndParse("ldap://[2001:db8::7]/c=GB?objectClass?one", urin(
                scheme("ldap"),
                hierarchicalPart(
                        authority(ipV6Address(hexadectet(0x2001), hexadectet(0xDB8), ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(0x7))),
                        segments(segment("c=GB"))),
                query("objectClass?one")
        ));
    }

    @Test
    public void mailtoExample() throws Exception {
        assertAsStringAsUriAndParse("mailto:John.Doe@example.com", urin(
                scheme("mailto"),
                hierarchicalPart(rootlessSegments(segment("John.Doe@example.com")))));
    }

    @Test
    public void newsExample() throws Exception {
        assertAsStringAsUriAndParse("news:comp.infosystems.www.servers.unix", urin(
                scheme("news"),
                hierarchicalPart(
                        rootlessSegments(segment("comp.infosystems.www.servers.unix")))));
    }

    @Test
    public void telExample() throws Exception {
        assertAsStringAsUriAndParse("tel:+1-816-555-1212", urin(
                scheme("tel"),
                hierarchicalPart(rootlessSegments(segment("+1-816-555-1212")))));
    }

    @Test
    public void telnetExample() throws Exception {
        assertAsStringAsUriAndParse("telnet://192.0.2.16:80/", urin(
                scheme("telnet"),
                hierarchicalPart(
                        authority(ipV4Address(octet(192), octet(0), octet(2), octet(16)), port("80")),
                        segments())
        ));
    }

    @Test
    public void urnExample() throws Exception {
        assertAsStringAsUriAndParse("urn:oasis:names:specification:docbook:dtd:xml:4.1.2", urin(
                scheme("urn"),
                hierarchicalPart(rootlessSegments(segment("oasis:names:specification:docbook:dtd:xml:4.1.2")))));
    }

    @Test
    public void removeDotSegmentsExample1() throws Exception {
        assertThat(
                RelativeReference.relativeReference(AbsoluteSegments.segments(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g"))).asString(),
                equalTo("/a/g"));
        assertThat(
                RelativeReference.relativeReference(AbsoluteSegments.segments(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g"))).asUri(),
                equalTo(new URI("/a/g")));
    }

    @Test
    public void removeDotSegmentsExample2() throws Exception {
        assertThat(
                RelativeReference.relativeReference(RootlessSegments.rootlessSegments(segment("mid"), segment("content=5"), DOT_DOT, segment("6"))).asString(),
                equalTo("mid/6"));
        assertThat(
                RelativeReference.relativeReference(RootlessSegments.rootlessSegments(segment("mid"), segment("content=5"), DOT_DOT, segment("6"))).asUri(),
                equalTo(new URI("mid/6")));
    }

    private static void assertAsStringAsUriAndParse(final String stringRepresentation, final Urin urinRepresentation) throws URISyntaxException, ParseException {
        assertThat(urinRepresentation.asString(), equalTo(stringRepresentation));
        assertThat(urinRepresentation.asUri(), equalTo(new URI(stringRepresentation)));
        assertThat(Urin.parse(stringRepresentation), equalTo(urinRepresentation));
    }
}
