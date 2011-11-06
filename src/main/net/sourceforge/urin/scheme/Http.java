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
import java.util.Collection;
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

    private static final Scheme SCHEME = scheme("http");
    private static final Port DEFAULT_PORT = port(80);

    public static Urin http(final Host host) {
        return urin(SCHEME, hierarchicalPartAbsolute(authority(host)));
    }

    public static Urin http(final Host host, final Port port) {
        return urin(SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port)));
    }

    public static Urin http(final Host host, final Segments segments) {
        return urin(SCHEME, hierarchicalPartAbsolute(authority(host), segments));
    }

    public static Urin http(final Host host, final Port port, final Segments segments) {
        return urin(SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port), segments));
    }

    private static Authority authorityWithNormalisedDefaultPort(final Host host, final Port port) {
        return DEFAULT_PORT.equals(port) ? authority(host) : authority(host, port);
    }

    public static Urin http(final Host host, final Segments segments, final Query query) {
        return urin(SCHEME, hierarchicalPartAbsolute(authority(host), segments), query);
    }

    public static Urin http(final Host host, final Segments segments, final QueryParameters queryParameters) {
        return urin(SCHEME, hierarchicalPartAbsolute(authority(host), segments), queryParameters.asQuery());
    }

    public static Urin http(final Host host, final Port port, final Segments segments, final Query query) {
        return urin(SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port), segments), query);
    }

    public static Urin http(final Host host, final Port port, final Segments segments, final QueryParameters queryParameters) {
        return urin(SCHEME, hierarchicalPartAbsolute(authorityWithNormalisedDefaultPort(host, port), segments), queryParameters.asQuery());
    }

    public static QueryParameter queryParameter(final String name, final String value) {
        return new QueryParameter(name, value);
    }

    public static QueryParameters queryParameters(final QueryParameter... queryParameters) {
        return queryParameters(asList(queryParameters));
    }

    public static QueryParameters queryParameters(final Iterable<QueryParameter> queryParameters) {
        return new QueryParameters(queryParameters);
    }

    public static final class QueryParameter {
        private final String name;
        private final String value;

        QueryParameter(final String name, final String value) {
            this.name = name;
            this.value = value;
        }
    }

    public static final class QueryParameters {

        private final Collection<QueryParameter> queryParameters;

        public QueryParameters(final Iterable<QueryParameter> queryParameters) {
            this.queryParameters = new ArrayList<QueryParameter>() {{
                for (QueryParameter queryParameter : queryParameters) {
                    add(queryParameter);
                }
            }};
        }

        private Query asQuery() {
            List<PercentEncodable> percentEncodables = new ArrayList<PercentEncodable>(queryParameters.size());
            for (QueryParameter queryParameter : queryParameters) {
                percentEncodables.add(PercentEncodable.percentEncodableDelimitedValue('=', percentEncodableSubstitutedValue(' ', '+', queryParameter.name), percentEncodableSubstitutedValue(' ', '+', queryParameter.value)));
            }
            return query(percentEncodableDelimitedValue(';', percentEncodableDelimitedValue('&', percentEncodables)));
        }
    }
}
