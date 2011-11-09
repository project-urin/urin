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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPartAbsolute;
import static net.sourceforge.urin.PercentEncodable.percentEncodableDelimitedValue;
import static net.sourceforge.urin.PercentEncodable.percentEncodableSubstitutedValue;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.Urin.urin;

public class Http {

    private static final Scheme HTTP_SCHEME = scheme("http");
    private static final Port DEFAULT_HTTP_PORT = port(80);
    private static final Scheme HTTPS_SCHEME = scheme("https");
    private static final Port DEFAULT_HTTPS_PORT = port(443);

    public static Urin http(final Host host) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host)));
    }

    public static Urin http(final Host host, final Port port) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT)));
    }

    public static Urin http(final Host host, final Segments segments) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host), segments));
    }

    public static Urin http(final Host host, final Port port, final Segments segments) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT), segments));
    }

    public static Urin http(final Host host, final Segments segments, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host), segments), query);
    }

    public static Urin http(final Host host, final Port port, final Segments segments, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT), segments), query);
    }

    public static Urin http(final Host host, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host)), query);
    }

    public static Urin http(final Host host, final Port port, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT)), query);
    }

    public static Urin http(final Host host, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host)), fragment);
    }

    public static Urin http(final Host host, final Port port, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT)), fragment);
    }

    public static Urin http(final Host host, final Segments segments, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host), segments), fragment);
    }

    public static Urin http(final Host host, final Port port, final Segments segments, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT), segments), fragment);
    }

    public static Urin http(final Host host, final Segments segments, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host), segments), query, fragment);
    }

    public static Urin http(final Host host, final Port port, final Segments segments, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT), segments), query, fragment);
    }

    public static Urin http(final Host host, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authority(host)), query, fragment);
    }

    public static Urin http(final Host host, final Port port, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTP_PORT)), query, fragment);
    }

    public static Urin https(final Host host) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host)));
    }

    public static Urin https(final Host host, final Port port) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT)));
    }

    public static Urin https(final Host host, final Segments segments) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host), segments));
    }

    public static Urin https(final Host host, final Port port, final Segments segments) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT), segments));
    }

    public static Urin https(final Host host, final Segments segments, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host), segments), query);
    }

    public static Urin https(final Host host, final Port port, final Segments segments, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT), segments), query);
    }

    public static Urin https(final Host host, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host)), query);
    }

    public static Urin https(final Host host, final Port port, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT)), query);
    }

    public static Urin https(final Host host, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host)), fragment);
    }

    public static Urin https(final Host host, final Port port, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT)), fragment);
    }

    public static Urin https(final Host host, final Segments segments, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host), segments), fragment);
    }

    public static Urin https(final Host host, final Port port, final Segments segments, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT), segments), fragment);
    }

    public static Urin https(final Host host, final Segments segments, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host), segments), query, fragment);
    }

    public static Urin https(final Host host, final Port port, final Segments segments, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT), segments), query, fragment);
    }

    public static Urin https(final Host host, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authority(host)), query, fragment);
    }

    public static Urin https(final Host host, final Port port, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port, DEFAULT_HTTPS_PORT)), query, fragment);
    }

    private static Authority authorityWithNormalisedDefaultPort(final Host host, final Port port, final Port defaultPort) {
        return defaultPort.equals(port) ? authority(host) : authority(host, port);
    }

    public static QueryParameter queryParameter(final String name, final String value) {
        return new QueryParameter(name, value);
    }

    public static Query queryParameters(final QueryParameter... queryParameters) {
        return queryParameters(asList(queryParameters));
    }

    public static Query queryParameters(final Iterable<QueryParameter> queryParameters) {
        final List<PercentEncodable> percentEncodables = new ArrayList<PercentEncodable>();
        for (QueryParameter queryParameter : queryParameters) {
            percentEncodables.add(PercentEncodable.percentEncodableDelimitedValue('=', percentEncodableSubstitutedValue(' ', '+', queryParameter.name), percentEncodableSubstitutedValue(' ', '+', queryParameter.value)));
        }
        return query(percentEncodableDelimitedValue(';', percentEncodableDelimitedValue('&', percentEncodables)));
    }

    public static final class QueryParameter {
        private final String name;
        private final String value;

        private QueryParameter(final String name, final String value) {
            this.name = name;
            this.value = value;
        }
    }

}
