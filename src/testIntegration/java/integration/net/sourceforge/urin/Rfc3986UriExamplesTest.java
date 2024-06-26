/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin;

import net.sourceforge.urin.Segment;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.Octet.octet;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.UrinAssert.assertAsStringAsUriAndParse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class Rfc3986UriExamplesTest {

    @Test
    void ftpExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "ftp://ftp.is.co.za/rfc/rfc1808.txt", scheme("ftp").urin(
                authority(registeredName("ftp.is.co.za")),
                path(segment("rfc"), segment("rfc1808.txt"))
        ));
    }

    @Test
    void httpExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "http://www.ietf.org/rfc/rfc2396.txt", scheme("http").urin(
                authority(registeredName("www.ietf.org")),
                path(segment("rfc"), segment("rfc2396.txt"))
        ));
    }

    @Test
    void ldapExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "ldap://[2001:db8::7]/c=GB?objectClass?one", scheme("ldap").urin(
                authority(ipV6Address(hexadectet(0x2001), hexadectet(0xDB8), ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(0x7))),
                path(segment("c=GB")),
                query("objectClass?one")
        ));
    }

    @Test
    void mailtoExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "mailto:John.Doe@example.com", scheme("mailto").urin(
                rootlessPath(segment("John.Doe@example.com"))));
    }

    @Test
    void newsExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "news:comp.infosystems.www.servers.unix", scheme("news").urin(
                rootlessPath(segment("comp.infosystems.www.servers.unix"))));
    }

    @Test
    void telExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "tel:+1-816-555-1212", scheme("tel").urin(
                rootlessPath(segment("+1-816-555-1212"))));
    }

    @Test
    void telnetExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "telnet://192.0.2.16:80/", scheme("telnet").urin(
                authority(ipV4Address(octet(192), octet(0), octet(2), octet(16)), port("80")),
                path()
        ));
    }

    @Test
    void urnExample() throws Exception {
        assertAsStringAsUriAndParse(aScheme(), "urn:oasis:names:specification:docbook:dtd:xml:4.1.2", scheme("urn").urin(
                rootlessPath(segment("oasis:names:specification:docbook:dtd:xml:4.1.2"))));
    }

    @Test
    void removeDotSegmentsExample1() throws Exception {
        assertThat(
                aScheme().relativeReference(path(segment("a"), segment("b"), segment("c"), Segment.dot(), Segment.dotDot(), Segment.dotDot(), segment("g"))).asString(),
                equalTo("/a/g"));
        assertThat(
                aScheme().relativeReference(path(segment("a"), segment("b"), segment("c"), Segment.dot(), Segment.dotDot(), Segment.dotDot(), segment("g"))).asUri(),
                equalTo(new URI("/a/g")));
    }

    @Test
    void removeDotSegmentsExample2() throws Exception {
        assertThat(
                aScheme().relativeReference(rootlessPath(segment("mid"), segment("content=5"), Segment.dotDot(), segment("6"))).asString(),
                equalTo("mid/6"));
        assertThat(
                aScheme().relativeReference(rootlessPath(segment("mid"), segment("content=5"), Segment.dotDot(), segment("6"))).asUri(),
                equalTo(new URI("mid/6")));
    }

}
