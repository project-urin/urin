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

import net.sourceforge.urin.Path;
import net.sourceforge.urin.Scheme;
import net.sourceforge.urin.Urin;
import org.junit.Test;

import java.net.URI;

import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986UriExamplesTest {
//    ftp://ftp.is.co.za/rfc/rfc1808.txt
//    http://www.ietf.org/rfc/rfc2396.txt
//    ldap://[2001:db8::7]/c=GB?objectClass?one
//    mailto:John.Doe@example.com
//     news:comp.infosystems.www.servers.unix
//     tel:+1-816-555-1212
//    telnet://192.0.2.16:80/
//     urn:oasis:names:specification:docbook:dtd:xml:4.1.2

    @Test
    public void telExample() throws Exception {
        final Path path = new Path("+1-816-555-1212");
        Urin urin = urin(new Scheme("tel"), hierarchicalPart(path));
        assertThat(urin.asString(), equalTo("tel:+1-816-555-1212"));
        assertThat(urin.asUri(), equalTo(new URI("tel:+1-816-555-1212")));
    }

    @Test
    public void newsExample() throws Exception {
        final Path path = new Path("comp.infosystems.www.servers.unix");
        Urin urin = urin(new Scheme("news"), hierarchicalPart(path));
        assertThat(urin.asString(), equalTo("news:comp.infosystems.www.servers.unix"));
        assertThat(urin.asUri(), equalTo(new URI("news:comp.infosystems.www.servers.unix")));
    }

    @Test
    public void urnExample() throws Exception {
        final Path path = new Path("oasis:names:specification:docbook:dtd:xml:4.1.2");
        Urin urin = urin(new Scheme("urn"), hierarchicalPart(path));
        assertThat(urin.asString(), equalTo("urn:oasis:names:specification:docbook:dtd:xml:4.1.2"));
        assertThat(urin.asUri(), equalTo(new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")));
    }
}
