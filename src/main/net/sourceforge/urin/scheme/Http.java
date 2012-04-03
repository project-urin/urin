/*
 * Copyright 2012 Mark Slater
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

import static java.util.Arrays.asList;
import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.Urin.urin;

public final class Http {

    private static final Scheme HTTP_SCHEME = scheme("http", port(80));
    private static final Scheme HTTPS_SCHEME = scheme("https", port(443));

    public static Urin http(final Host host) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path()));
    }

    public static Urin http(final Host host, final Port port) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path()));
    }

    public static Urin http(final Authority authority) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path()));
    }

    public static Urin http(final Host host, final AbsolutePath path) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path));
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path));
    }

    public static Urin http(final Authority authority, final AbsolutePath path) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path));
    }

    public static Urin http(final Host host, final AbsolutePath path, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path), query);
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path), query);
    }

    public static Urin http(final Authority authority, final AbsolutePath path, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path), query);
    }

    public static Urin http(final Host host, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path()), query);
    }

    public static Urin http(final Host host, final Port port, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path()), query);
    }

    public static Urin http(final Authority authority, final Query query) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path()), query);
    }

    public static Urin http(final Host host, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path()), fragment);
    }

    public static Urin http(final Host host, final Port port, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path()), fragment);
    }

    public static Urin http(final Authority authority, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path()), fragment);
    }

    public static Urin http(final Host host, final AbsolutePath path, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path), fragment);
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path), fragment);
    }

    public static Urin http(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path), fragment);
    }

    public static Urin http(final Host host, final AbsolutePath path, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path), query, fragment);
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path), query, fragment);
    }

    public static Urin http(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path), query, fragment);
    }

    public static Urin http(final Host host, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host), path()), query, fragment);
    }

    public static Urin http(final Host host, final Port port, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority(host, port), path()), query, fragment);
    }

    public static Urin http(final Authority authority, final Query query, final Fragment fragment) {
        return urin(HTTP_SCHEME, hierarchicalPart(authority, path()), query, fragment);
    }

    public static Urin https(final Host host) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path()));
    }

    public static Urin https(final Host host, final Port port) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path()));
    }

    public static Urin https(final Authority authority) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path()));
    }

    public static Urin https(final Host host, final AbsolutePath path) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path));
    }

    public static Urin https(final Host host, final Port port, final AbsolutePath path) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path));
    }

    public static Urin https(final Authority authority, final AbsolutePath path) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path));
    }

    public static Urin https(final Host host, final AbsolutePath path, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path), query);
    }

    public static Urin https(final Host host, final Port port, final AbsolutePath path, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path), query);
    }

    public static Urin https(final Authority authority, final AbsolutePath path, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path), query);
    }

    public static Urin https(final Host host, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path()), query);
    }

    public static Urin https(final Host host, final Port port, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path()), query);
    }

    public static Urin https(final Authority authority, final Query query) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path()), query);
    }

    public static Urin https(final Host host, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path()), fragment);
    }

    public static Urin https(final Host host, final Port port, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path()), fragment);
    }

    public static Urin https(final Authority authority, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path()), fragment);
    }

    public static Urin https(final Host host, final AbsolutePath path, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path), fragment);
    }

    public static Urin https(final Host host, final Port port, final AbsolutePath path, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path), fragment);
    }

    public static Urin https(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path), fragment);
    }

    public static Urin https(final Host host, final AbsolutePath path, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path), query, fragment);
    }

    public static Urin https(final Host host, final Port port, final AbsolutePath path, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path), query, fragment);
    }

    public static Urin https(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path), query, fragment);
    }

    public static Urin https(final Host host, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host), path()), query, fragment);
    }

    public static Urin https(final Host host, final Port port, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority(host, port), path()), query, fragment);
    }

    public static Urin https(final Authority authority, final Query query, final Fragment fragment) {
        return urin(HTTPS_SCHEME, hierarchicalPart(authority, path()), query, fragment);
    }

    public static QueryParameter queryParameter(final String name, final String value) {
        return new QueryParameter(name, value);
    }

    public static Query queryParameters(final QueryParameter... queryParameters) {
        return queryParameters(asList(queryParameters));
    }

    public static Query queryParameters(final Iterable<QueryParameter> queryParameters) {
        return new HttpQuery(queryParameters);
    }

    private static final class HttpQuery extends Query {
        private HttpQuery(final Iterable<QueryParameter> queryParameters) {
            super(PercentEncodable.percentEncodableDelimitedValue(';', PercentEncodable.percentEncodableDelimitedValue('&', new ArrayList<PercentEncodable>() {{
                for (QueryParameter queryParameter : queryParameters) {
                    add(PercentEncodable.percentEncodableDelimitedValue('=', PercentEncodable.percentEncodableSubstitutedValue(' ', '+', queryParameter.name), PercentEncodable.percentEncodableSubstitutedValue(' ', '+', queryParameter.value)));
                }
            }})));
        }
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
