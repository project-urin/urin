/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme;

import net.sourceforge.urin.Host;
import net.sourceforge.urin.Port;
import net.sourceforge.urin.Query;
import net.sourceforge.urin.Segments;
import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPartAbsolute;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.PortBuilder.aPortDifferentTo;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static net.sourceforge.urin.Urin.urin;
import static net.sourceforge.urin.scheme.Http.http;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HttpTest {
    @Test
    public void httpWithNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        assertThat(http(host), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host)))));
    }

    @Test
    public void httpWithPortButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port)))));
    }

    @Test
    public void port80IsNormalisedAway() throws Exception {
        Host host = aHost();
        Port port = port(80);
        assertThat(http(host, port), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host)))));
    }

    @Test
    public void httpWithPathButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        assertThat(http(host, segments), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments))));
    }

    @Test
    public void httpWithPathAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        Segments segments = aSegments();
        assertThat(http(host, port, segments), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port), segments))));
    }

    @Test
    public void httpWithPathButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(80);
        Segments segments = aSegments();
        assertThat(http(host, port, segments), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments))));
    }

    @Test
    public void httpWithPathAndQueryButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        assertThat(http(host, segments, query), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments), query)));
    }

    @Test
    public void httpWithPathAndQueryAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port, segments, query), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port), segments), query)));
    }

    @Test
    public void httpWithPathAndQueryAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = port(80);
        assertThat(http(host, port, segments, query), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments), query)));
    }

}
