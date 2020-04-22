/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.*;
import org.junit.jupiter.api.Test;

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
import static net.sourceforge.urin.scheme.http.HypertextScheme.parseHttpRelativeReference;
import static net.sourceforge.urin.scheme.http.HypertextScheme.parseHttpUrin;
import static net.sourceforge.urin.scheme.http.HypertextScheme.parseHttpUrinReference;
import static net.sourceforge.urin.scheme.http.QueryMatcher.convertsToQueryString;
import static net.sourceforge.urin.scheme.http.QueryParameterBuilder.aQueryParameter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpTest {
    @Test
    void httpWithNoPathProducesCorrectUrin() {
        Host host = aHost();
        assertThat(http(host), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    void httpWithPortButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port), equalTo(HTTP.urin(authority(host, port), path())));
    }

    @Test
    void httpWithQueryButNoPathProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(http(host, query), equalTo(HTTP.urin(authority(host), path(), query)));
    }

    @Test
    void httpWithQueryAndPortButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        HttpQuery query = anHttpQuery();
        assertThat(http(host, port, query), equalTo(HTTP.urin(authority(host, port), path(), query)));
    }

    @Test
    void port80IsNormalisedAway() {
        Host host = aHost();
        Port port = port(80);
        assertThat(http(host, port), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    void httpWithPathButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(host, path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(host, port, path), equalTo(HTTP.urin(authority(host, port), path)));
    }

    @Test
    void httpWithPathButAndDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(host, port, path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndQueryButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(http(host, path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithPathAndQueryAndPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port, path, query), equalTo(HTTP.urin(authority(host, port), path, query)));
    }

    @Test
    void httpWithPathAndQueryAndDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        assertThat(http(host, port, path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithOnlyFragmentProducesCorrectUrin() {
        Host host = aHost();
        Fragment<String> fragment = aFragment();
        assertThat(http(host, fragment), equalTo(HTTP.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpWithPortAndFragmentButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        Fragment<String> fragment = aFragment();
        assertThat(http(host, port, fragment), equalTo(HTTP.urin(authority(host, port), path(), fragment)));
    }

    @Test
    void httpWithQueryAndFragmentButNoPathProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(http(host, query, fragment), equalTo(HTTP.urin(authority(host), path(), query, fragment)));
    }

    @Test
    void httpWithQueryAndPortAndFragmentButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(http(host, port, query, fragment), equalTo(HTTP.urin(authority(host, port), path(), query, fragment)));
    }

    @Test
    void httpWithPathAndFragmentButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(http(host, path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(http(host, port, path, fragment), equalTo(HTTP.urin(authority(host, port), path, fragment)));
    }

    @Test
    void httpWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(http(host, port, path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(http(host, path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(80));
        Fragment<String> fragment = aFragment();
        assertThat(http(host, port, path, query, fragment), equalTo(HTTP.urin(authority(host, port), path, query, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        Fragment<String> fragment = aFragment();
        assertThat(http(host, port, path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    void singleQueryParameterCorrectlyConvertsToQuery() {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .", ".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    void multipleQueryParametersCorrectlyConvertsToQuery() {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .", ".+.&.;.=. ."), queryParameter(".+.&.;.=. .", ".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.&.%2B.%26.%3B.%3D.+.=.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    void singleValuelessQueryParameterCorrectlyConvertsToQuery() {
        assertThat(queryParameters(queryParameter(".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.")));
    }

    @Test
    void multiplyValuelessQueryParameterCorrectlyConvertsToQuery() {
        assertThat(queryParameters(queryParameter(".+.&.;.=. ."), queryParameter(".+.&.;.=. .")), convertsToQueryString(equalTo(".%2B.%26.%3B.%3D.+.&.%2B.%26.%3B.%3D.+.")));
    }

    @Test
    void singleValuelessQueryParameterWithSingleCharacterCorrectlyConvertsToQuery() {
        assertThat(queryParameters(queryParameter(" ")), convertsToQueryString(equalTo("+")));
    }

    @Test
    void httpsWithNoPathProducesCorrectUrin() {
        Host host = aHost();
        assertThat(Https.https(host), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    void httpsWithPortButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port), equalTo(HTTPS.urin(authority(host, port), path())));
    }

    @Test
    void port443IsNormalisedAway() {
        Host host = aHost();
        Port port = port(443);
        assertThat(Https.https(host, port), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    void httpsWithPathButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(host, path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(host, port, path), equalTo(HTTPS.urin(authority(host, port), path)));
    }

    @Test
    void httpsWithPathButAndDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(host, port, path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndQueryButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(host, path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithPathAndQueryAndPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port, path, query), equalTo(HTTPS.urin(authority(host, port), path, query)));
    }

    @Test
    void httpsWithQueryButNoPortProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(host, query), equalTo(HTTPS.urin(authority(host), path(), query)));
    }

    @Test
    void httpsWithQueryAndPortProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port, query), equalTo(HTTPS.urin(authority(host, port), path(), query)));
    }

    @Test
    void httpsWithPathAndQueryAndDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        assertThat(Https.https(host, port, path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithJustFragmentProducesCorrectUrin() {
        Host host = aHost();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, fragment), equalTo(HTTPS.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpsWithPortAndFragmentButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, fragment), equalTo(HTTPS.urin(authority(host, port), path(), fragment)));
    }

    @Test
    void httpsWithPathAndFragmentButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, fragment), equalTo(HTTPS.urin(authority(host, port), path, fragment)));
    }

    @Test
    void httpsWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, query, fragment), equalTo(HTTPS.urin(authority(host, port), path, query, fragment)));
    }

    @Test
    void httpsWithQueryAndFragmentButNoPortProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, query, fragment), equalTo(HTTPS.urin(authority(host), path(), query, fragment)));
    }

    @Test
    void httpsWithQueryAndPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Port port = aPortDifferentTo(port(443));
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, query, fragment), equalTo(HTTPS.urin(authority(host, port), path(), query, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpWithAuthorityButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(80));
        assertThat(http(authority(host, port)), equalTo(HTTP.urin(authority(host, port), path())));
    }

    @Test
    void httpWithAuthorityAndQueryButNoPathProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(http(authority(host), query), equalTo(HTTP.urin(authority(host), path(), query)));
    }

    @Test
    void httpPort80IsNormalisedAwayFromAuthority() {
        Host host = aHost();
        Port port = port(80);
        assertThat(http(authority(host, port)), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    void httpWithPathAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(authority(host), path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndAuthorityWithDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(authority(host, port), path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndQueryAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(http(authority(host), path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithPathAndQueryAndAuthorityWithDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        assertThat(http(authority(host, port), path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithOnlyFragmentAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), fragment), equalTo(HTTP.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpWithQueryAndFragmentAndAuthorityButNoPathProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), query, fragment), equalTo(HTTP.urin(authority(host), query, fragment)));
    }

    @Test
    void httpWithPathAndFragmentAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndFragmentAndAuthorityWithDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(80);
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(http(authority(host, port), path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndFragmentAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndAuthorityWithDefaultPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(80);
        Fragment<String> fragment = aFragment();
        assertThat(http(authority(host, port), path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpsWithAuthorityButNoPathProducesCorrectUrin() {
        Host host = aHost();
        Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(authority(host, port)), equalTo(HTTPS.urin(authority(host, port), path())));
    }

    @Test
    void httpsWithAuthorityAndQueryButNoPathProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(authority(host), query), equalTo(HTTPS.urin(authority(host), path(), query)));
    }

    @Test
    void httpsPort443IsNormalisedAwayFromAuthority() {
        Host host = aHost();
        Port port = port(443);
        assertThat(Https.https(authority(host, port)), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    void httpsWithPathAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(authority(host), path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndAuthorityWithDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(authority(host, port), path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndQueryAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        assertThat(Https.https(authority(host), path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithPathAndQueryAndAuthorityWithDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        assertThat(Https.https(authority(host, port), path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithOnlyFragmentAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), fragment), equalTo(HTTPS.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpsWithQueryAndFragmentAndAuthorityButNoPathProducesCorrectUrin() {
        Host host = aHost();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), query, fragment), equalTo(HTTPS.urin(authority(host), path(), query, fragment)));
    }

    @Test
    void httpsWithPathAndFragmentAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndFragmentAndAuthorityWithDefaultPortProducesCorrectUrin() {
        Host host = aHost();
        Port port = port(443);
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host, port), path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndFragmentAndAuthorityProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndAuthorityWithDefaultPortAndFragmentProducesCorrectUrin() {
        Host host = aHost();
        AbsolutePath<String> path = anAbsolutePath();
        HttpQuery query = anHttpQuery();
        Port port = port(443);
        Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host, port), path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void roundTrippedHttpUrlsAreEqual() throws Exception {
        Urin<?, ?, ?> httpUrin = http(anAuthority(), anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithTrailingEmptySegmentAreEqual() throws Exception {
        Urin<?, ?, ?> httpUrin = http(anAuthority(), path("blah", ""), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithSingleEmptySegmentAreEqual() throws Exception {
        Urin<?, ?, ?> httpUrin = http(anAuthority(), path(""), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithEmptyPathAreEqual() throws Exception {
        Urin<?, ?, ?> httpUrin = http(anAuthority(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithTwoEmptySegmentsAreEqual() throws Exception {
        Urin<?, ?, ?> httpUrin = http(anAuthority(), path("", ""), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsViaStaticAreEqual() throws Exception {
        Urin<?, ?, ?> httpUrin = http(anAuthority(), anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedRelativeReferencesViaStaticAreEqual() throws Exception {
        RelativeReference<String, HttpQuery, Fragment<String>> httpRelativeReference = HTTP.relativeReference(anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpRelativeReference(httpRelativeReference.asString()), equalTo(httpRelativeReference));
    }

    @Test
    void roundTrippedUrinReferencesViaStaticAreEqual() throws Exception {
        UrinReference<String, HttpQuery, Fragment<String>> httpRelativeReference = HTTP.relativeReference(anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpUrinReference(httpRelativeReference.asString()), equalTo(httpRelativeReference));
    }

    @Test
    void anInvalidQueryParameterThrowsParseException() {
        final ParseException parseException = assertThrows(ParseException.class, () -> HTTP.parseUrin("http://somewhere?name=value=broken"));
        assertThat(parseException.getMessage(), equalTo("Invalid query parameter - expected maximum of two elements in [[name, value, broken]]"));
    }

    @Test
    void handlesEncodingOfSpaceInHttpQueryParameters() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?name=value+with+space").query(), equalTo(queryParameters(queryParameter("name", "value with space"))));
    }

    @Test
    void handlesDecodingOfPercentEncodedPlusInHttpQueryParameters() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?name=value%2Bwith%2Bplus").query(), equalTo(queryParameters(queryParameter("name", "value+with+plus"))));
    }
}
