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

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.Octet.octet;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986UriExamplesTest {

    @Test
    public void ftpExample() throws Exception {
        Urin urin = urin(
                scheme("ftp"),
                hierarchicalPart(
                        authority(registeredName("ftp.is.co.za")),
                        Segments.segments(segment("rfc"), segment("rfc1808.txt")))
        );
        assertThat(urin.asString(), equalTo("ftp://ftp.is.co.za/rfc/rfc1808.txt"));
        assertThat(urin.asUri(), equalTo(new URI("ftp://ftp.is.co.za/rfc/rfc1808.txt")));
    }

    @Test
    public void httpExample() throws Exception {
        Urin urin = urin(
                scheme("http"),
                hierarchicalPart(
                        authority(registeredName("www.ietf.org")),
                        Segments.segments(segment("rfc"), segment("rfc2396.txt")))
        );
        assertThat(urin.asString(), equalTo("http://www.ietf.org/rfc/rfc2396.txt"));
        assertThat(urin.asUri(), equalTo(new URI("http://www.ietf.org/rfc/rfc2396.txt")));
    }

    @Test
    public void ldapExample() throws Exception {
        Urin urin = urin(
                scheme("ldap"),
                hierarchicalPart(
                        authority(ipV6Address(hexadectet(0x2001), hexadectet(0xDB8), ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(0x7))),
                        AbsoluteSegments.segments(segment("c=GB"))),
                Query.query("objectClass?one")
        );
        assertThat(urin.asString(), equalTo("ldap://[2001:db8::7]/c=GB?objectClass?one"));
        assertThat(urin.asUri(), equalTo(new URI("ldap://[2001:db8::7]/c=GB?objectClass?one")));
    }

    @Test
    public void mailtoExample() throws Exception {
        Urin urin = urin(
                scheme("mailto"),
                HierarchicalPart.hierarchicalPart(RelativeSegments.rootlessSegments(segment("John.Doe@example.com"))));
        assertThat(urin.asString(), equalTo("mailto:John.Doe@example.com"));
        assertThat(urin.asUri(), equalTo(new URI("mailto:John.Doe@example.com")));
    }

    @Test
    public void newsExample() throws Exception {
        Urin urin = urin(scheme("news"), HierarchicalPart.hierarchicalPart(RelativeSegments.rootlessSegments(segment("comp.infosystems.www.servers.unix"))));
        assertThat(urin.asString(), equalTo("news:comp.infosystems.www.servers.unix"));
        assertThat(urin.asUri(), equalTo(new URI("news:comp.infosystems.www.servers.unix")));
    }

    @Test
    public void telExample() throws Exception {
        Urin urin = urin(scheme("tel"), HierarchicalPart.hierarchicalPart(RelativeSegments.rootlessSegments(segment("+1-816-555-1212"))));
        assertThat(urin.asString(), equalTo("tel:+1-816-555-1212"));
        assertThat(urin.asUri(), equalTo(new URI("tel:+1-816-555-1212")));
    }

    @Test
    public void telnetExample() throws Exception {
        Urin urin = urin(
                scheme("telnet"),
                hierarchicalPart(
                        authority(ipV4Address(octet(192), octet(0), octet(2), octet(16)), port("80")),
                        AbsoluteSegments.segments())
        );
        assertThat(urin.asString(), equalTo("telnet://192.0.2.16:80/"));
        assertThat(urin.asUri(), equalTo(new URI("telnet://192.0.2.16:80/")));
    }

    @Test
    public void urnExample() throws Exception {
        Urin urin = urin(scheme("urn"), HierarchicalPart.hierarchicalPart(RelativeSegments.rootlessSegments(segment("oasis:names:specification:docbook:dtd:xml:4.1.2"))));
        assertThat(urin.asString(), equalTo("urn:oasis:names:specification:docbook:dtd:xml:4.1.2"));
        assertThat(urin.asUri(), equalTo(new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")));
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
                RelativeReference.relativeReference(RelativeSegments.rootlessSegments(segment("mid"), segment("content=5"), DOT_DOT, segment("6"))).asString(),
                equalTo("mid/6"));
        assertThat(
                RelativeReference.relativeReference(RelativeSegments.rootlessSegments(segment("mid"), segment("content=5"), DOT_DOT, segment("6"))).asUri(),
                equalTo(new URI("mid/6")));
    }
}
