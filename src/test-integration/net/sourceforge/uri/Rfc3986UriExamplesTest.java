/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.uri;

import net.sourceforge.urin.*;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;

import static net.sourceforge.urin.AbEmptyPath.absolutePath;
import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.Octet.octet;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986UriExamplesTest {
//     ftp://ftp.is.co.za/rfc/rfc1808.txt
//     http://www.ietf.org/rfc/rfc2396.txt
//    ldap://[2001:db8::7]/c=GB?objectClass?one
//     mailto:John.Doe@example.com
//     news:comp.infosystems.www.servers.unix
//     tel:+1-816-555-1212
//     telnet://192.0.2.16:80/
//     urn:oasis:names:specification:docbook:dtd:xml:4.1.2

    @Test
    public void ftpExample() throws Exception {
        Urin urin = urin(
                new Scheme("ftp"),
                hierarchicalPart(
                        authority(registeredName("ftp.is.co.za")),
                        absolutePath(new Segment("rfc"), new Segment("rfc1808.txt")))
        );
        assertThat(urin.asString(), equalTo("ftp://ftp.is.co.za/rfc/rfc1808.txt"));
        assertThat(urin.asUri(), equalTo(new URI("ftp://ftp.is.co.za/rfc/rfc1808.txt")));
    }

    @Test
    public void httpExample() throws Exception {
        Urin urin = urin(
                new Scheme("http"),
                hierarchicalPart(
                        authority(registeredName("www.ietf.org")),
                        absolutePath(new Segment("rfc"), new Segment("rfc2396.txt")))
        );
        assertThat(urin.asString(), equalTo("http://www.ietf.org/rfc/rfc2396.txt"));
        assertThat(urin.asUri(), equalTo(new URI("http://www.ietf.org/rfc/rfc2396.txt")));
    }

    @Test
    @Ignore
    public void ldapExample() throws Exception {
        Urin urin = urin(
                new Scheme("ldap"),
                hierarchicalPart(
                        authority(ipV6Address(hexadectet(0x2001), hexadectet(0xDB8), ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(0x7))),
                        absolutePath(new Segment("c=GB"))),
                new Query("objectClass?one")
        );
        assertThat(urin.asString(), equalTo("ldap://[2001:db8::7]/c=GB?objectClass?one"));
        assertThat(urin.asUri(), equalTo(new URI("ldap://[2001:db8::7]/c=GB?objectClass?one")));
    }

    @Test
    public void mailtoExample() throws Exception {
        Urin urin = urin(
                new Scheme("mailto"),
                hierarchicalPart(
                        new PathRootlessAbsoluteOrEmpty(new NonEmptySegment("John.Doe@example.com")))
        );
        assertThat(urin.asString(), equalTo("mailto:John.Doe@example.com"));
        assertThat(urin.asUri(), equalTo(new URI("mailto:John.Doe@example.com")));
    }

    @Test
    public void newsExample() throws Exception {
        final PathRootlessAbsoluteOrEmpty pathRootlessAbsoluteOrEmpty = new PathRootlessAbsoluteOrEmpty(new NonEmptySegment("comp.infosystems.www.servers.unix"));
        Urin urin = urin(new Scheme("news"), hierarchicalPart(pathRootlessAbsoluteOrEmpty));
        assertThat(urin.asString(), equalTo("news:comp.infosystems.www.servers.unix"));
        assertThat(urin.asUri(), equalTo(new URI("news:comp.infosystems.www.servers.unix")));
    }

    @Test
    public void telExample() throws Exception {
        final PathRootlessAbsoluteOrEmpty pathRootlessAbsoluteOrEmpty = new PathRootlessAbsoluteOrEmpty(new NonEmptySegment("+1-816-555-1212"));
        Urin urin = urin(new Scheme("tel"), hierarchicalPart(pathRootlessAbsoluteOrEmpty));
        assertThat(urin.asString(), equalTo("tel:+1-816-555-1212"));
        assertThat(urin.asUri(), equalTo(new URI("tel:+1-816-555-1212")));
    }

    @Test
    public void telnetExample() throws Exception {
        Urin urin = urin(
                new Scheme("telnet"),
                hierarchicalPart(
                        authority(ipV4Address(octet(192), octet(0), octet(2), octet(16)), new Port("80")),
                        absolutePath())
        );
        assertThat(urin.asString(), equalTo("telnet://192.0.2.16:80/"));
        assertThat(urin.asUri(), equalTo(new URI("telnet://192.0.2.16:80/")));
    }

    @Test
    public void urnExample() throws Exception {
        final PathRootlessAbsoluteOrEmpty pathRootlessAbsoluteOrEmpty = new PathRootlessAbsoluteOrEmpty(new NonEmptySegment("oasis:names:specification:docbook:dtd:xml:4.1.2"));
        Urin urin = urin(new Scheme("urn"), hierarchicalPart(pathRootlessAbsoluteOrEmpty));
        assertThat(urin.asString(), equalTo("urn:oasis:names:specification:docbook:dtd:xml:4.1.2"));
        assertThat(urin.asUri(), equalTo(new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")));
    }
}
