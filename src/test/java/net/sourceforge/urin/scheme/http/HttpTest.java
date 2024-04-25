/*
 * Copyright 2024 Mark Slater
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
import static net.sourceforge.urin.AuthorityBuilder.aJavaUriCompatibleAuthority;
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
import static net.sourceforge.urin.scheme.http.HypertextScheme.*;
import static net.sourceforge.urin.scheme.http.QueryMatcher.convertsToQueryString;
import static net.sourceforge.urin.scheme.http.QueryParameterBuilder.aQueryParameter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpTest {
    @Test
    void httpWithNoPathProducesCorrectUrin() {
        final Host host = aHost();
        assertThat(http(host), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    void httpWithPortButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port), equalTo(HTTP.urin(authority(host, port), path())));
    }

    @Test
    void httpWithQueryButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        assertThat(http(host, query), equalTo(HTTP.urin(authority(host), path(), query)));
    }

    @Test
    void httpWithQueryAndPortButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(80));
        final HttpQuery query = anHttpQuery();
        assertThat(http(host, port, query), equalTo(HTTP.urin(authority(host, port), path(), query)));
    }

    @Test
    void port80IsNormalisedAway() {
        final Host host = aHost();
        final Port port = port(80);
        assertThat(http(host, port), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    void httpWithPathButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(host, path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(80));
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(host, port, path), equalTo(HTTP.urin(authority(host, port), path)));
    }

    @Test
    void httpWithPathButAndDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(80);
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(host, port, path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndQueryButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        assertThat(http(host, path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithPathAndQueryAndPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = aPortDifferentTo(port(80));
        assertThat(http(host, port, path, query), equalTo(HTTP.urin(authority(host, port), path, query)));
    }

    @Test
    void httpWithPathAndQueryAndDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(80);
        assertThat(http(host, port, path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithOnlyFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, fragment), equalTo(HTTP.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpWithPortAndFragmentButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(80));
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, port, fragment), equalTo(HTTP.urin(authority(host, port), path(), fragment)));
    }

    @Test
    void httpWithQueryAndFragmentButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, query, fragment), equalTo(HTTP.urin(authority(host), path(), query, fragment)));
    }

    @Test
    void httpWithQueryAndPortAndFragmentButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(80));
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, port, query, fragment), equalTo(HTTP.urin(authority(host, port), path(), query, fragment)));
    }

    @Test
    void httpWithPathAndFragmentButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(80));
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, port, path, fragment), equalTo(HTTP.urin(authority(host, port), path, fragment)));
    }

    @Test
    void httpWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(80);
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, port, path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = aPortDifferentTo(port(80));
        final Fragment<String> fragment = aFragment();
        assertThat(http(host, port, path, query, fragment), equalTo(HTTP.urin(authority(host, port), path, query, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(80);
        final Fragment<String> fragment = aFragment();
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
        final Host host = aHost();
        assertThat(Https.https(host), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    void httpsWithPortButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port), equalTo(HTTPS.urin(authority(host, port), path())));
    }

    @Test
    void port443IsNormalisedAway() {
        final Host host = aHost();
        final Port port = port(443);
        assertThat(Https.https(host, port), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    void httpsWithPathButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(host, path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(443));
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(host, port, path), equalTo(HTTPS.urin(authority(host, port), path)));
    }

    @Test
    void httpsWithPathButAndDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(443);
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(host, port, path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndQueryButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        assertThat(Https.https(host, path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithPathAndQueryAndPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port, path, query), equalTo(HTTPS.urin(authority(host, port), path, query)));
    }

    @Test
    void httpsWithQueryButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        assertThat(Https.https(host, query), equalTo(HTTPS.urin(authority(host), path(), query)));
    }

    @Test
    void httpsWithQueryAndPortProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        final Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(host, port, query), equalTo(HTTPS.urin(authority(host, port), path(), query)));
    }

    @Test
    void httpsWithPathAndQueryAndDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(443);
        assertThat(Https.https(host, port, path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithJustFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, fragment), equalTo(HTTPS.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpsWithPortAndFragmentButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(443));
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, fragment), equalTo(HTTPS.urin(authority(host, port), path(), fragment)));
    }

    @Test
    void httpsWithPathAndFragmentButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(443));
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, fragment), equalTo(HTTPS.urin(authority(host, port), path, fragment)));
    }

    @Test
    void httpsWithPathAndFragmentButAndDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(443);
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndFragmentButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = aPortDifferentTo(port(443));
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, query, fragment), equalTo(HTTPS.urin(authority(host, port), path, query, fragment)));
    }

    @Test
    void httpsWithQueryAndFragmentButNoPortProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, query, fragment), equalTo(HTTPS.urin(authority(host), path(), query, fragment)));
    }

    @Test
    void httpsWithQueryAndPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        final Port port = aPortDifferentTo(port(443));
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, query, fragment), equalTo(HTTPS.urin(authority(host, port), path(), query, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndDefaultPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(443);
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(host, port, path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpWithAuthorityButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(80));
        assertThat(http(authority(host, port)), equalTo(HTTP.urin(authority(host, port), path())));
    }

    @Test
    void httpWithAuthorityAndQueryButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        assertThat(http(authority(host), query), equalTo(HTTP.urin(authority(host), path(), query)));
    }

    @Test
    void httpPort80IsNormalisedAwayFromAuthority() {
        final Host host = aHost();
        final Port port = port(80);
        assertThat(http(authority(host, port)), equalTo(HTTP.urin(authority(host), path())));
    }

    @Test
    void httpWithPathAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(authority(host), path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndAuthorityWithDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(80);
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(http(authority(host, port), path), equalTo(HTTP.urin(authority(host), path)));
    }

    @Test
    void httpWithPathAndQueryAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        assertThat(http(authority(host), path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithPathAndQueryAndAuthorityWithDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(80);
        assertThat(http(authority(host, port), path, query), equalTo(HTTP.urin(authority(host), path, query)));
    }

    @Test
    void httpWithOnlyFragmentAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), fragment), equalTo(HTTP.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpWithQueryAndFragmentAndAuthorityButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), query, fragment), equalTo(HTTP.urin(authority(host), query, fragment)));
    }

    @Test
    void httpWithPathAndFragmentAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndFragmentAndAuthorityWithDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(80);
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(http(authority(host, port), path, fragment), equalTo(HTTP.urin(authority(host), path, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndFragmentAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(http(authority(host), path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpWithPathAndQueryAndAuthorityWithDefaultPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(80);
        final Fragment<String> fragment = aFragment();
        assertThat(http(authority(host, port), path, query, fragment), equalTo(HTTP.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpsWithAuthorityButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = aPortDifferentTo(port(443));
        assertThat(Https.https(authority(host, port)), equalTo(HTTPS.urin(authority(host, port), path())));
    }

    @Test
    void httpsWithAuthorityAndQueryButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        assertThat(Https.https(authority(host), query), equalTo(HTTPS.urin(authority(host), path(), query)));
    }

    @Test
    void httpsPort443IsNormalisedAwayFromAuthority() {
        final Host host = aHost();
        final Port port = port(443);
        assertThat(Https.https(authority(host, port)), equalTo(HTTPS.urin(authority(host), path())));
    }

    @Test
    void httpsWithPathAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(authority(host), path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndAuthorityWithDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(443);
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(Https.https(authority(host, port), path), equalTo(HTTPS.urin(authority(host), path)));
    }

    @Test
    void httpsWithPathAndQueryAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        assertThat(Https.https(authority(host), path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithPathAndQueryAndAuthorityWithDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(443);
        assertThat(Https.https(authority(host, port), path, query), equalTo(HTTPS.urin(authority(host), path, query)));
    }

    @Test
    void httpsWithOnlyFragmentAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), fragment), equalTo(HTTPS.urin(authority(host), path(), fragment)));
    }

    @Test
    void httpsWithQueryAndFragmentAndAuthorityButNoPathProducesCorrectUrin() {
        final Host host = aHost();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), query, fragment), equalTo(HTTPS.urin(authority(host), path(), query, fragment)));
    }

    @Test
    void httpsWithPathAndFragmentAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndFragmentAndAuthorityWithDefaultPortProducesCorrectUrin() {
        final Host host = aHost();
        final Port port = port(443);
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host, port), path, fragment), equalTo(HTTPS.urin(authority(host), path, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndFragmentAndAuthorityProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host), path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void httpsWithPathAndQueryAndAuthorityWithDefaultPortAndFragmentProducesCorrectUrin() {
        final Host host = aHost();
        final AbsolutePath<String> path = anAbsolutePath();
        final HttpQuery query = anHttpQuery();
        final Port port = port(443);
        final Fragment<String> fragment = aFragment();
        assertThat(Https.https(authority(host, port), path, query, fragment), equalTo(HTTPS.urin(authority(host), path, query, fragment)));
    }

    @Test
    void roundTrippedHttpUrlsAreEqual() throws Exception {
        final Urin<?, ?, ?> httpUrin = http(anAuthority(), anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithTrailingEmptySegmentAreEqual() throws Exception {
        final Urin<?, ?, ?> httpUrin = http(anAuthority(), path("blah", ""), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithSingleEmptySegmentAreEqual() throws Exception {
        final Urin<?, ?, ?> httpUrin = http(anAuthority(), path(""), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithEmptyPathAreEqual() throws Exception {
        final Urin<?, ?, ?> httpUrin = http(anAuthority(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsWithTwoEmptySegmentsAreEqual() throws Exception {
        final Urin<?, ?, ?> httpUrin = http(anAuthority(), path("", ""), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(HTTP.parseUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrlsViaStaticAreEqual() throws Exception {
        final Urin<?, ?, ?> httpUrin = http(anAuthority(), anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpUrin(httpUrin.asString()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedHttpUrisViaStaticAreEqual() throws Exception {
        final Urin<?, ?, ?> httpUrin = http(aJavaUriCompatibleAuthority(), anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpUrin(httpUrin.asUri()), equalTo(httpUrin));
    }

    @Test
    void roundTrippedRelativeReferencesViaStaticAreEqual() throws Exception {
        final RelativeReference<String, HttpQuery, Fragment<String>> httpRelativeReference = HTTP.relativeReference(anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpRelativeReference(httpRelativeReference.asString()), equalTo(httpRelativeReference));
    }

    @Test
    void roundTrippedRelativeReferenceUrisViaStaticAreEqual() throws Exception {
        final RelativeReference<String, HttpQuery, Fragment<String>> httpRelativeReference = HTTP.relativeReference(anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpRelativeReference(httpRelativeReference.asUri()), equalTo(httpRelativeReference));
    }

    @Test
    void roundTrippedUrinReferencesViaStaticAreEqual() throws Exception {
        final UrinReference<String, HttpQuery, Fragment<String>> httpRelativeReference = HTTP.relativeReference(anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpUrinReference(httpRelativeReference.asString()), equalTo(httpRelativeReference));
    }

    @Test
    void roundTrippedUrinReferenceUrisViaStaticAreEqual() throws Exception {
        final UrinReference<String, HttpQuery, Fragment<String>> httpRelativeReference = HTTP.relativeReference(anAbsolutePath(), queryParameters(aQueryParameter(), aQueryParameter()), aFragment());
        assertThat(parseHttpUrinReference(httpRelativeReference.asUri()), equalTo(httpRelativeReference));
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

    @Test
    void canParseEmptyStringQueryParameters() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?name=").query(), equalTo(queryParameters(queryParameter("name", ""))));
    }

    @Test
    void canParseValuelessQueryParameter() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?name").query(), equalTo(queryParameters(queryParameter("name"))));
    }

    @Test
    void canParseNamelessQueryParameter() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?=foo").query(), equalTo(queryParameters(queryParameter("", "foo"))));
    }

    @Test
    void canParseNamelessValuelessQueryParameter() throws Exception {
        assertThat(HTTP.parseUrin("http://somewhere?=").query(), equalTo(queryParameters(queryParameter("", ""))));
    }
}
