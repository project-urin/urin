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

import net.sourceforge.urin.Port;
import net.sourceforge.urin.Query;
import net.sourceforge.urin.SchemeWithDefaultPort;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public abstract class HypertextScheme extends SchemeWithDefaultPort {
    HypertextScheme(final String name, final Port defaultPort) {
        super(name, defaultPort);
    }

    /**
     * Factory method for name/value pair query parameters.
     *
     * @param name  the name part of the query parameter.
     * @param value the value part of the query parameter.
     * @return a {@code QueryParameter} representing the given name and value.
     */
    public static QueryParameter queryParameter(final String name, final String value) {
        return new QueryParameter(name, value);
    }

    /**
     * Factory method for HTTP and HTTPs encoded query components made up of name/value pairs.
     *
     * @param queryParameters {@code QueryParameter}s that will be represented by this query.
     * @return a {@code Query} representing the given query parameters.
     */
    public static Query queryParameters(final QueryParameter... queryParameters) {
        return queryParameters(asList(queryParameters));
    }

    /**
     * Factory method for HTTP and HTTPs encoded query components made up of name/value pairs.
     *
     * @param queryParameters {@code Iterable} of {@code QueryParameter}s that will be represented by this query.
     * @return a {@code Query} representing the given query parameters.
     */
    public static Query queryParameters(final Iterable<QueryParameter> queryParameters) {
        return new HttpQuery(queryParameters);
    }

    private static final class HttpQuery extends Query {
        private HttpQuery(final Iterable<Http.QueryParameter> queryParameters) {
            super(PercentEncodable.percentEncodableDelimitedValue(';', PercentEncodable.percentEncodableDelimitedValue('&', new ArrayList<PercentEncodable>() {{
                for (QueryParameter queryParameter : queryParameters) {
                    add(PercentEncodable.percentEncodableDelimitedValue('=', PercentEncodable.percentEncodableSubstitutedValue(' ', '+', queryParameter.name), PercentEncodable.percentEncodableSubstitutedValue(' ', '+', queryParameter.value)));
                }
            }})));
        }
    }

    /**
     * Value type for specifying HTTP query parameter name/value pairs.
     *
     * @see net.sourceforge.urin.scheme.Http#queryParameter(String, String)
     */
    public static final class QueryParameter {
        private final String name;
        private final String value;

        private QueryParameter(final String name, final String value) {
            this.name = name;
            this.value = value;
        }
    }
}
