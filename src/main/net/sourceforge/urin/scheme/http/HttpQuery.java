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
import net.sourceforge.urin.Parser;
import net.sourceforge.urin.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static java.util.Arrays.asList;

/**
 * A query component of a URI with HTTP/HTTPS specific sub-encoding.
 */
public final class HttpQuery extends Query<Iterable<HttpQuery.QueryParameter>> implements Iterable<HttpQuery.QueryParameter> {

    static final Parser<HttpQuery> QUERY_PARSER = new Parser<HttpQuery>() {
        public HttpQuery parse(final String rawQuery) throws ParseException {
            return new HttpQuery(new ArrayList<QueryParameter>() {{
                for (QueryParameter queryParameter : HTTP_QUERY_PERCENT_ENCODING.decode(rawQuery)) {
                    add(queryParameter);
                }
            }});
        }
    };

    private static final PercentEncoding<Iterable<QueryParameter>> HTTP_QUERY_PERCENT_ENCODING = encodeQueryParameters(
            PercentEncoding.percentEncodingDelimitedValue('&',
                    PercentEncoding.percentEncodingDelimitedValue(';',
                            percentEncodedQueryParameter(
                                    PercentEncoding.percentEncodingDelimitedValue('=',
                                            PercentEncoding.percentEncodingSubstitutedValue(' ', '+', PERCENT_ENCODING))
                            ))));

    private HttpQuery(final Iterable<QueryParameter> queryParameters) {
        super(Collections.unmodifiableList(new ArrayList<QueryParameter>() {{
            for (QueryParameter queryParameter : queryParameters) {
                if (queryParameter == null) {
                    throw new NullPointerException("Cannot instantiate QueryParameters with null queryParameter");
                }
                add(queryParameter);
            }
        }}), HTTP_QUERY_PERCENT_ENCODING);
    }

    private static PercentEncoding<Iterable<QueryParameter>> encodeQueryParameters(final PercentEncoding<Iterable<Iterable<QueryParameter>>> percentEncoding) {
        return new PercentEncoding<Iterable<QueryParameter>>() {

            @Override
            public String encode(final Iterable<QueryParameter> notEncoded) {
                return percentEncoding.encode(new ArrayList<Iterable<QueryParameter>>() {{
                    for (QueryParameter queryParameter : notEncoded) {
                        add(Arrays.asList(queryParameter));
                    }
                }});
            }

            @Override
            public Iterable<QueryParameter> decode(final String encoded) throws ParseException {
                return new ArrayList<QueryParameter>() {{
                    for (Iterable<QueryParameter> queryParameters : percentEncoding.decode(encoded)) {
                        for (QueryParameter queryParameter : queryParameters) {
                            add(queryParameter);
                        }
                    }
                }};
            }

            @Override
            public PercentEncoding<Iterable<QueryParameter>> additionallyEncoding(final char additionallyEncodedCharacter) {
                return encodeQueryParameters(percentEncoding.additionallyEncoding(additionallyEncodedCharacter));
            }

        };
    }

    private static PercentEncoding<QueryParameter> percentEncodedQueryParameter(final PercentEncoding<Iterable<String>> partsEncoding) {
        return new PercentEncoding<QueryParameter>() {
            @Override
            public String encode(final QueryParameter notEncoded) {
                return notEncoded.encodeWith(partsEncoding);
            }

            @Override
            public QueryParameter decode(final String encoded) throws ParseException {
                final QueryParameter result;
                Iterator<String> iterator = partsEncoding.decode(encoded).iterator();
                if (!iterator.hasNext()) {
                    throw new ParseException("Invalid query parameter String [" + encoded + "]");
                }
                final String name = iterator.next();
                if (!iterator.hasNext()) {
                    result = queryParameter(name);
                } else {
                    result = queryParameter(name, iterator.next());
                    if (iterator.hasNext()) {
                        throw new ParseException("Invalid query parameter - expected only one occurrence of '=' in [" + encoded + "]");
                    }
                }
                return result;
            }

            @Override
            public PercentEncoding<QueryParameter> additionallyEncoding(final char additionallyEncodedCharacter) {
                return percentEncodedQueryParameter(partsEncoding.additionallyEncoding(additionallyEncodedCharacter));
            }

        };
    }

    /**
     * Factory method for name/value pair query parameters.
     *
     * @param name  the name part of the query parameter.
     * @param value the value part of the query parameter.
     * @return a {@code QueryParameter} representing the given name and value.
     */
    public static QueryParameter queryParameter(final String name, final String value) {
        return new NameAndValueQueryParameter(name, value);
    }

    /**
     * Factory method for name only query parameters.
     *
     * @param name the name part of the query parameter.
     * @return a {@code QueryParameter} representing the given name.
     */
    public static QueryParameter queryParameter(final String name) {
        return new NameOnlyQueryParameter(name);
    }

    /**
     * Factory method for HTTP and HTTPs encoded query components made up of name/value pairs.
     *
     * @param queryParameters {@code QueryParameter}s that will be represented by this query.
     * @return a {@code HttpQuery} representing the given query parameters.
     */
    public static HttpQuery queryParameters(final QueryParameter... queryParameters) {
        return queryParameters(asList(queryParameters));
    }

    /**
     * Factory method for HTTP and HTTPs encoded query components made up of name/value pairs.
     *
     * @param queryParameters {@code Iterable} of {@code QueryParameter}s that will be represented by this query.
     * @return a {@code HttpQuery} representing the given query parameters.
     */
    public static HttpQuery queryParameters(final Iterable<QueryParameter> queryParameters) {
        return new HttpQuery(queryParameters);
    }

    public Iterator<HttpQuery.QueryParameter> iterator() {
        return value().iterator();
    }

    /**
     * Value type for specifying HTTP query parameter name/value pairs.
     *
     * @see HttpQuery#queryParameter(String, String)
     */
    public static abstract class QueryParameter {

        private QueryParameter() {
        }

        abstract String encodeWith(final PercentEncoding<Iterable<String>> partsEncoding);
    }

    private static final class NameAndValueQueryParameter extends QueryParameter {
        private final String name;
        private final String value;

        NameAndValueQueryParameter(final String name, final String value) {
            if (name == null) {
                throw new NullPointerException("Cannot instantiate QueryParameter with null name");
            }
            this.name = name;
            if (value == null) {
                throw new NullPointerException("Cannot instantiate QueryParameter with null value");
            }
            this.value = value;
        }

        @Override
        String encodeWith(final PercentEncoding<Iterable<String>> partsEncoding) {
            return partsEncoding.encode(Arrays.asList(name, value));
        }

        @Override
        public String toString() {
            return "QueryParameter{name='" + name + "', value='" + value + "'}";
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NameAndValueQueryParameter that = (NameAndValueQueryParameter) o;

            return name.equals(that.name) && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }
    }

    private static final class NameOnlyQueryParameter extends QueryParameter {
        private final String name;

        NameOnlyQueryParameter(final String name) {
            if (name == null) {
                throw new NullPointerException("Cannot instantiate QueryParameter with null name");
            }
            this.name = name;
        }

        @Override
        String encodeWith(final PercentEncoding<Iterable<String>> partsEncoding) {
            return partsEncoding.encode(Arrays.asList(name));
        }

        @Override
        public String toString() {
            return "QueryParameter{name='" + name + "'}";
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NameOnlyQueryParameter that = (NameOnlyQueryParameter) o;

            return name.equals(that.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
