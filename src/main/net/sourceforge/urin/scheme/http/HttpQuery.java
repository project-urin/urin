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

import net.sourceforge.urin.MakingDecoder;
import net.sourceforge.urin.ParseException;
import net.sourceforge.urin.Query;
import net.sourceforge.urin.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static java.util.Arrays.asList;

/**
 * A query component of a URI with HTTP/HTTPS specific sub-encoding.
 */
public final class HttpQuery extends Query<Iterable<HttpQuery.QueryParameter>> implements Iterable<HttpQuery.QueryParameter> {

    private static final PercentEncodingPartial<Iterable<QueryParameter>, String> HTTP_QUERY_PERCENT_ENCODING_PARTIAL = encodeQueryParameters(
            PercentEncodingPartial.<Iterable<QueryParameter>, String>percentEncodingDelimitedValue(
                    '&',
                    PercentEncodingPartial.<QueryParameter, String>percentEncodingDelimitedValue(
                            ';',
                            percentEncodedQueryParameter(PercentEncodingPartial.<String, String>percentEncodingDelimitedValue(
                                    '=',
                                    PercentEncodingPartial.percentEncodingSubstitutedValue(' ', '+'))))));

    public static MakingDecoder<HttpQuery, Iterable<QueryParameter>, String> httpQueryMakingDecoder() {
        return new MakingDecoder<HttpQuery, Iterable<QueryParameter>, String>(HTTP_QUERY_PERCENT_ENCODING_PARTIAL) {
            @Override
            protected HttpQuery makeOne(Iterable<QueryParameter> queryParameters) {
                return new HttpQuery(queryParameters);
            }
        };
    }

    private HttpQuery(final Iterable<QueryParameter> queryParameters) {
        super(Collections.unmodifiableList(new ArrayList<QueryParameter>() {{
            for (QueryParameter queryParameter : queryParameters) {
                if (queryParameter == null) {
                    throw new NullPointerException("Cannot instantiate QueryParameters with null queryParameter");
                }
                add(queryParameter);
            }
        }}), HTTP_QUERY_PERCENT_ENCODING_PARTIAL);
    }

    private static <T> PercentEncodingPartial<Iterable<QueryParameter>, T> encodeQueryParameters(final PercentEncodingPartial<Iterable<Iterable<QueryParameter>>, T> childPercentEncodingPartial) {
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<Iterable<QueryParameter>, Iterable<Iterable<QueryParameter>>>() {
            public Iterable<Iterable<QueryParameter>> encode(final Iterable<QueryParameter> queryParameters) {
                return new ArrayList<Iterable<QueryParameter>>() {{
                    for (QueryParameter queryParameter : queryParameters) {
                        add(Arrays.asList(queryParameter));
                    }
                }};
            }

            public Iterable<QueryParameter> decode(final Iterable<Iterable<QueryParameter>> iterables) {
                return new ArrayList<QueryParameter>() {{
                    for (Iterable<QueryParameter> queryParameters : iterables) {
                        for (QueryParameter queryParameter : queryParameters) {
                            add(queryParameter);
                        }
                    }
                }};
            }
        });
    }

    private static <T> PercentEncodingPartial<QueryParameter, T> percentEncodedQueryParameter(final PercentEncodingPartial<Iterable<String>, T> childPercentEncodingPartial) {
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<QueryParameter, Iterable<String>>() {
            public Iterable<String> encode(QueryParameter queryParameter) {
                return queryParameter.encoded();
            }

            public QueryParameter decode(Iterable<String> strings) throws ParseException {
                final QueryParameter result;
                Iterator<String> iterator = strings.iterator();
                if (!iterator.hasNext()) {
                    throw new ParseException("Invalid query parameter String [" + strings + "]");
                }
                final String name = iterator.next();
                if (!iterator.hasNext()) {
                    result = queryParameter(name);
                } else {
                    result = queryParameter(name, iterator.next());
                    if (iterator.hasNext()) {
                        throw new ParseException("Invalid query parameter - expected maximum of two elements in [" + strings + "]");
                    }
                }
                return result;
            }
        });
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

        abstract Iterable<String> encoded();

        /**
         * Gets the (non-encoded) name of this query parameter as a {@code String}.
         *
         * @return the (non-encoded) name of this query parameter as a {@code String}.
         */
        public abstract String name();

        /**
         * Returns true if {@code value()} can be called on this {@code QueryParameter}.  This method
         * returns false for {@code QueryParameter}s that do not have a value component.
         *
         * @return true if {@code value()} can be called on this {@code QueryParameter}.
         */
        public abstract boolean hasValue();

        /**
         * Gets the value component of this {@code QueryParameter}, as a {@code String}, if it has one, or throws {@code UnsupportedOperationException} otherwise.
         * <p/>
         * The existence of a value component can be tested by calling {@code hasValue()}.
         *
         * @return the value component of this {@code QueryParameter}.
         * @throws UnsupportedOperationException if this is a {@code QueryParameter} that does not have a value component.
         */
        public abstract String value();
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
        Iterable<String> encoded() {
            return Arrays.asList(name, value);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean hasValue() {
            return true;
        }

        @Override
        public String value() {
            return value;
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
        Iterable<String> encoded() {
            return Arrays.asList(name);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public String value() {
            throw new UnsupportedOperationException("Attempt to get value from a QueryParameter that does not have one.");
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
