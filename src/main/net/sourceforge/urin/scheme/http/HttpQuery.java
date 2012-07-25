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

import net.sourceforge.urin.ParseException;
import net.sourceforge.urin.Query;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public final class HttpQuery extends Query {

    static final QueryParser QUERY_PARSER = new QueryParser() {
        public Query parse(final String rawQuery) throws ParseException {
            return new HttpQuery(new ArrayList<QueryParameter>() {{
                for (String queryParameter : rawQuery.split("[;&]")) {
                    if (queryParameter.contains("=")) {
                        String[] nameValuePair = queryParameter.split("=");
                        if (nameValuePair.length != 2) {
                            throw new ParseException("Invalid query parameter - expected only one occurrence of '=' in " + queryParameter);
                        } else {
                            add(queryParameter(decodeSpaces(nameValuePair[0]), decodeSpaces(nameValuePair[1])));
                        }
                    } else {
                        add(queryParameter(decodeSpaces(queryParameter)));
                    }
                }
            }});
        }
    };

    private static String decodeSpaces(final String rawValue) throws ParseException {
        String[] rawValues = rawValue.split("\\+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rawValues.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(percentDecode(rawValues[i]));
        }
        return result.toString();
    }

    HttpQuery(final Iterable<QueryParameter> queryParameters) {
        super(PercentEncodable.percentEncodableDelimitedValue(';', PercentEncodable.percentEncodableDelimitedValue('&', new ArrayList<PercentEncodable>() {{
            for (QueryParameter queryParameter : queryParameters) {
                add(queryParameter.asPercentEncodable());
            }
        }})));
    }

    /**
     * Factory method for name/value pair query parameters.
     *
     * @param name  the name part of the query parameter.
     * @param value the value part of the query parameter.
     * @return a {@code QueryParameter} representing the given name and value.
     */
    public static QueryParameter queryParameter(final String name, final String value) {
        return new QueryParameter() {
            @Override
            PercentEncodable asPercentEncodable() {
                return PercentEncodable.percentEncodableDelimitedValue('=', PercentEncodable.percentEncodableSubstitutedValue(' ', '+', name), PercentEncodable.percentEncodableSubstitutedValue(' ', '+', value));
            }
        };
    }

    /**
     * Factory method for name only query parameters.
     *
     * @param name the name part of the query parameter.
     * @return a {@code QueryParameter} representing the given name.
     */
    public static QueryParameter queryParameter(final String name) {
        return new QueryParameter() {
            @Override
            PercentEncodable asPercentEncodable() {
                return PercentEncodable.percentEncodableDelimitedValue('=', PercentEncodable.percentEncodableSubstitutedValue(' ', '+', name));
            }
        };
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

    /**
     * Value type for specifying HTTP query parameter name/value pairs.
     *
     * @see HttpQuery#queryParameter(String, String)
     */
    public static abstract class QueryParameter {

        private QueryParameter() {
        }

        abstract PercentEncodable asPercentEncodable();
    }
}