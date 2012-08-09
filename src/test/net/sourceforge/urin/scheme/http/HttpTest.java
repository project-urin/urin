/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.*;
import org.junit.Assert;
import org.junit.Test;

import static net.sourceforge.urin.AbsolutePathBuilder.anAbsolutePath;
import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.PortBuilder.aPortDifferentTo;
import static net.sourceforge.urin.scheme.http.Http.HTTP;
import static net.sourceforge.urin.scheme.http.Http.http;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;
import static net.sourceforge.urin.scheme.http.HttpQueryBuilder.anHttpQuery;
import static net.sourceforge.urin.scheme.http.Https.HTTPS;
import static net.sourceforge.urin.scheme.http.QueryMatcher.convertsToQueryString;
import static net.sourceforge.urin.scheme.http.QueryParameterBuilder.aQueryParameter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class HttpTest {
    @Test
    public void httpWithNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        assertThat(Http.http(host), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    public void httpWithPortButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port), equalTo(HTTP.urin(authority(host, port), path())));
    }

    @Test
    public void httpWithQueryButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(Http.http(host, query), equalTo(HTTP.urin(authority(host), path(), query)));
    }

    @Test
    public void httpWithQueryAndPortButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        HttpQuery query = anHttpQuery();
        assertThat(http(host, port, query), equalTo(HTTP.urin(authority(host, port), path(), query)));
    }

    @Test
    public void port80IsNormalisedAway() throws Exception {
        Host host = aHost();
        Port port = port(80);
        assertThat(http(host, port), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    public void httpWithPathButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        assertThat(http(host, path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    public void httpWithPathAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        AbsolutePath path = anAbsolutePath();
        assertThat(http(host, port, path), equalTo(HTTP.urin(authority(host, port), path)));
    }

    @Test
    public void httpWithPathButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath path = anAbsolutePath();
        assertThat(http(host, port, path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    public void httpWithPathAndQueryButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(http(host, path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    public void httpWithPathAndQueryAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port, path, query), equalTo(HTTP.urin(authority(host, port), path, query)));
    }

    @Test
    public void httpWithPathAndQueryAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        assertThat(http(host, port, path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    public void httpWithOnlyFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Fragment fragment = aFragment();
        assertThat(http(host, fragment), equalTo(HTTP.urin(authority(host), path(), fragment)));
    }

    @Test
    public void httpWithPortAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        Fragment fragment = aFragment();
        assertThat(http(host, port, fragment), equalTo(HTTP.urin(authority(host, port), path(), fragment)));
    }

    @Test
    public void httpWithQueryAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(http(host, query, fragment), equalTo(HTTP.urin(authority(host), path(), query, fragment)));
    }

    @Test
    public void httpWithQueryAndPortAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(http(host, port, query, fragment), equalTo(HTTP.urin(authority(host, port), path(), query, fragment)));
    }

    @Test
    public void httpWithPathAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(http(host, path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpWithPathAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(http(host, port, path, fragment), equalTo(HTTP.urin(authority(host, port), path, fragment)));
    }

    @Test
    public void httpWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(http(host, port, path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(http(host, path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(80));
        Fragment fragment = aFragment();
        assertThat(http(host, port, path, query, fragment), equalTo(HTTP.urin(authority(host, port), path, query, fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        Fragment fragment = aFragment();
        assertThat(http(host, port, path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void singleQueryParameterCorrectlyConvertsToQuery() throws Exception {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .", ".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    public void multipleQueryParametersCorrectlyConvertsToQuery() throws Exception {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .", ".+.&.;.=. ."), queryParameter(".+.&.;.=. .", ".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.&.%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    public void singleValuelessQueryParameterCorrectlyConvertsToQuery() throws Exception {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.")));
    }

    @Test
    public void multiplyValuelessQueryParameterCorrectlyConvertsToQuery() throws Exception {
        assertThat(queryParameters(queryParameter(".+.&.;.=. ."), queryParameter(".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.&.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    public void httpsWithNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        assertThat(Https.https(host), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    public void httpsWithPortButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port), equalTo(HTTPS.urin(authority(host, port), path())));
    }

    @Test
    public void port443IsNormalisedAway() throws Exception {
        Host host = aHost();
        Port port = port(443);
        assertThat(Https.https(host, port), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    public void httpsWithPathButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        assertThat(Https.https(host, path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    public void httpsWithPathAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        AbsolutePath path = anAbsolutePath();
        assertThat(Https.https(host, port, path), equalTo(HTTPS.urin(authority(host, port), path)));
    }

    @Test
    public void httpsWithPathButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath path = anAbsolutePath();
        assertThat(Https.https(host, port, path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    public void httpsWithPathAndQueryButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(host, path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    public void httpsWithPathAndQueryAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port, path, query), equalTo(HTTPS.urin(authority(host, port), path, query)));
    }

    @Test
    public void httpsWithQueryButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(host, query), equalTo(HTTPS.urin(authority(host), path(), query)));
    }

    @Test
    public void httpsWithQueryAndPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port, query), equalTo(HTTPS.urin(authority(host, port), path(), query)));
    }

    @Test
    public void httpsWithPathAndQueryAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        assertThat(Https.https(host, port, path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    public void httpsWithJustFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Fragment fragment = aFragment();
        assertThat(Https.https(host, fragment), equalTo(HTTPS.urin(authority(host), path(), fragment)));
    }

    @Test
    public void httpsWithPortAndFragmentButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        Fragment fragment = aFragment();
        assertThat(Https.https(host, port, fragment), equalTo(HTTPS.urin(authority(host, port), path(), fragment)));
    }

    @Test
    public void httpsWithPathAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(Https.https(host, path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpsWithPathAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(Https.https(host, port, path, fragment), equalTo(HTTPS.urin(authority(host, port), path, fragment)));
    }

    @Test
    public void httpsWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(Https.https(host, port, path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(Https.https(host, path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        Fragment fragment = aFragment();
        assertThat(Https.https(host, port, path, query, fragment), equalTo(HTTPS.urin(authority(host, port), path, query, fragment)));
    }

    @Test
    public void httpsWithQueryAndFragmentButNoPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(Https.https(host, query, fragment), equalTo(HTTPS.urin(authority(host), path(), query, fragment)));
    }

    @Test
    public void httpsWithQueryAndPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        Fragment fragment = aFragment();
        assertThat(Https.https(host, port, query, fragment), equalTo(HTTPS.urin(authority(host, port), path(), query, fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        Fragment fragment = aFragment();
        assertThat(Https.https(host, port, path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void httpWithAuthorityButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(authority(host, port)), equalTo(HTTP.urin(authority(host, port), path())));
    }

    @Test
    public void httpWithAuthorityAndQueryButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(http(authority(host), query), equalTo(HTTP.urin(authority(host), path(), query)));
    }

    @Test
    public void httpPort80IsNormalisedAwayFromAuthority() throws Exception {
        Host host = aHost();
        Port port = port(80);
        assertThat(http(authority(host, port)), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    public void httpWithPathAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        assertThat(http(authority(host), path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    public void httpWithPathAndAuthorityWithDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath path = anAbsolutePath();
        assertThat(http(authority(host, port), path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    public void httpWithPathAndQueryAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(http(authority(host), path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    public void httpWithPathAndQueryAndAuthorityWithDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        assertThat(http(authority(host, port), path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    public void httpWithOnlyFragmentAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Fragment fragment = aFragment();
        assertThat(http(authority(host), fragment), equalTo(HTTP.urin(authority(host), path(), fragment)));
    }

    @Test
    public void httpWithQueryAndFragmentAndAuthorityButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(http(authority(host), query, fragment), equalTo(HTTP.urin(authority(host), path(), query, fragment)));
    }

    @Test
    public void httpWithPathAndFragmentAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(http(authority(host), path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpWithPathAndFragmentAndAuthorityWithDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(http(authority(host, port), path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndFragmentAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(http(authority(host), path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void httpWithPathAndQueryAndAuthorityWithDefaultPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        Fragment fragment = aFragment();
        assertThat(http(authority(host, port), path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void httpsWithAuthorityButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(authority(host, port)), equalTo(HTTPS.urin(authority(host, port), path())));
    }

    @Test
    public void httpsWithAuthorityAndQueryButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(authority(host), query), equalTo(HTTPS.urin(authority(host), path(), query)));
    }

    @Test
    public void httpsPort443IsNormalisedAwayFromAuthority() throws Exception {
        Host host = aHost();
        Port port = port(443);
        assertThat(Https.https(authority(host, port)), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    public void httpsWithPathAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        assertThat(Https.https(authority(host), path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    public void httpsWithPathAndAuthorityWithDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath path = anAbsolutePath();
        assertThat(Https.https(authority(host, port), path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    public void httpsWithPathAndQueryAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(authority(host), path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    public void httpsWithPathAndQueryAndAuthorityWithDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        assertThat(Https.https(authority(host, port), path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    public void httpsWithOnlyFragmentAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Fragment fragment = aFragment();
        assertThat(Https.https(authority(host), fragment), equalTo(HTTPS.urin(authority(host), path(), fragment)));
    }

    @Test
    public void httpsWithQueryAndFragmentAndAuthorityButNoPathProducesCorrectUrin() throws Exception {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(Https.https(authority(host), query, fragment), equalTo(HTTPS.urin(authority(host), path(), query, fragment)));
    }

    @Test
    public void httpsWithPathAndFragmentAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(Https.https(authority(host), path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpsWithPathAndFragmentAndAuthorityWithDefaultPortProducesCorrectUrin() throws Exception {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(Https.https(authority(host, port), path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndFragmentAndAuthorityProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment fragment = aFragment();
        assertThat(Https.https(authority(host), path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void httpsWithPathAndQueryAndAuthorityWithDefaultPortAndFragmentProducesCorrectUrin() throws Exception {
        Host host = aHost();
        AbsolutePath path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        Fragment fragment = aFragment();
        assertThat(Https.https(authority(host, port), path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    public void roundTrippedHttpUrlsAreEqual() throws Exception {
        Urin httpUrin = http(anAuthority(), anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    public void anInvalidQueryParameterThrowsAnException() throws Exception {
        try {
            HTTP.parseUrin("http://somewhere?name=value=somethingbroken");
            fail("Expected a ParseException to be thrown");
        } catch (ParseException e) {
            Assert.assertThat(e.getMessage(), equalTo("Invalid query parameter - expected only one occurrence of '=' in [name=value=somethingbroken]"));
        }
    }

    @Test
    public void handlesEncodingOfSpaceInHttpQueryParameters() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?name=value+with+space").query(), equalTo((Query) queryParameters(queryParameter("name", "value with space"))));
    }

    @Test
    public void handlesDecodingOfPercentEncodedPlusInHttpQueryParameters() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?name=value%2Bwith%2Bplus").query(), equalTo((Query) queryParameters(queryParameter("name", "value+with+plus"))));
    }
}
