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

import net.sourceforge.urin.*;
import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPartAbsolute;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.PortBuilder.aPortDifferentTo;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static net.sourceforge.urin.Urin.urin;
import static net.sourceforge.urin.scheme.Http.*;
import static net.sourceforge.urin.scheme.QueryMatcher.convertsToQueryString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
    public void httpWithQueryButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Query query = aQuery();
        assertThat(http(host, query), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host)), query)));
    }

    @Test
    public void httpWithQueryAndPortButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        Query query = aQuery();
        assertThat(http(host, port, query), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port)), query)));
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

    @Test
    public void httpWithOnlyFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Fragment fragment = aFragment();
        assertThat(http(host, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host)), fragment)));
    }

    @Test
    public void httpWithPortAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        Fragment fragment = aFragment();
        assertThat(http(host, port, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port)), fragment)));
    }

    @Test
    public void httpWithQueryAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(http(host, query, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host)), query, fragment)));
    }

    @Test
    public void httpWithQueryAndPortAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(http(host, port, query, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port)), query, fragment)));
    }

    @Test
    public void httpWithPathAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(http(host, segments, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments), fragment)));
    }

    @Test
    public void httpWithPathAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(http(host, port, segments, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port), segments), fragment)));
    }

    @Test
    public void httpWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(80);
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(http(host, port, segments, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments), fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(http(host, segments, query, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments), query, fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = aPortDifferentTo(port(80));
        Fragment fragment = aFragment();
        assertThat(http(host, port, segments, query, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host, port), segments), query, fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = port(80);
        Fragment fragment = aFragment();
        assertThat(http(host, port, segments, query, fragment), equalTo(urin(scheme("http"), hierarchicalPartAbsolute(authority(host), segments), query, fragment)));
    }

    @Test
    public void multipleQueryParametersCorrectlyConvertsToQuery() throws Exception {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .", ".+.&.;.=. ."), queryParameter(".+.&.;.=. .", ".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.&.%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    public void singleQueryParameterCorrectlyConvertsToQuery() throws Exception {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .", ".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    public void httpsWithNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        assertThat(https(host), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host)))));
    }

    @Test
    public void httpsWithPortButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        assertThat(https(host, port), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port)))));
    }

    @Test
    public void port443IsNormalisedAway() throws Exception {
        Host host = aHost();
        Port port = port(443);
        assertThat(https(host, port), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host)))));
    }

    @Test
    public void httpsWithPathButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        assertThat(https(host, segments), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments))));
    }

    @Test
    public void httpsWithPathAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        Segments segments = aSegments();
        assertThat(https(host, port, segments), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port), segments))));
    }

    @Test
    public void httpsWithPathButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(443);
        Segments segments = aSegments();
        assertThat(https(host, port, segments), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments))));
    }

    @Test
    public void httpsWithPathAndQueryButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        assertThat(https(host, segments, query), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments), query)));
    }

    @Test
    public void httpsWithPathAndQueryAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = aPortDifferentTo(port(443));
        assertThat(https(host, port, segments, query), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port), segments), query)));
    }

    @Test
    public void httpsWithQueryButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Query query = aQuery();
        assertThat(https(host, query), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host)), query)));
    }

    @Test
    public void httpsWithQueryAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Query query = aQuery();
        Port port = aPortDifferentTo(port(443));
        assertThat(https(host, port, query), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port)), query)));
    }

    @Test
    public void httpsWithPathAndQueryAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = port(443);
        assertThat(https(host, port, segments, query), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments), query)));
    }

    @Test
    public void httpsWithJustFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Fragment fragment = aFragment();
        assertThat(https(host, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host)), fragment)));
    }

    @Test
    public void httpsWithPortAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        Fragment fragment = aFragment();
        assertThat(https(host, port, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port)), fragment)));
    }

    @Test
    public void httpsWithPathAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(https(host, segments, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments), fragment)));
    }

    @Test
    public void httpsWithPathAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(https(host, port, segments, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port), segments), fragment)));
    }

    @Test
    public void httpsWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(443);
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(https(host, port, segments, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments), fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(https(host, segments, query, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments), query, fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = aPortDifferentTo(port(443));
        Fragment fragment = aFragment();
        assertThat(https(host, port, segments, query, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port), segments), query, fragment)));
    }

    @Test
    public void httpsWithQueryAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(https(host, query, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host)), query, fragment)));
    }

    @Test
    public void httpsWithQueryAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Query query = aQuery();
        Port port = aPortDifferentTo(port(443));
        Fragment fragment = aFragment();
        assertThat(https(host, port, query, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host, port)), query, fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Segments segments = aSegments();
        Query query = aQuery();
        Port port = port(443);
        Fragment fragment = aFragment();
        assertThat(https(host, port, segments, query, fragment), equalTo(urin(scheme("https"), hierarchicalPartAbsolute(authority(host), segments), query, fragment)));
    }

}
