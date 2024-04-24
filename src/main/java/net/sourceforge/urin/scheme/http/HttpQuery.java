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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;

/**
 * A query component of a URI with HTTP/HTTPS specific sub-encoding.
 */
public final class HttpQuery extends Query<Iterable<HttpQuery.QueryParameter>> implements Iterable<HttpQuery.QueryParameter> {

    private static final PercentEncodingPartial<Iterable<QueryParameter>, String> HTTP_QUERY_PERCENT_ENCODING_PARTIAL = encodeQueryParameters(
            PercentEncodingPartial.percentEncodingDelimitedValue(
                    '&',
                    PercentEncodingPartial.percentEncodingDelimitedValue(
                            ';',
                            percentEncodedQueryParameter(PercentEncodingPartial.percentEncodingDelimitedValue(
                                    '=',
                                    PercentEncodingPartial.percentEncodingSubstitutedValue(' ', '+'))))));
    private static final QueryParameter VALUELESS_EMPTY_NAMED_QUERY_PARAMETER = queryParameter("");

    /**
     * Factory method for {@code MakingDecoder}s of {@code HttpQuery}s
     *
     * @return a {@code MakingDecoder} of {@code String} {@code HttpQuery}s
     */
    public static MakingDecoder<HttpQuery, Iterable<QueryParameter>, String> httpQueryMakingDecoder() {
        return new MakingDecoder<HttpQuery, Iterable<QueryParameter>, String>(HTTP_QUERY_PERCENT_ENCODING_PARTIAL) {
            @Override
            protected HttpQuery makeOne(final Iterable<QueryParameter> queryParameters) {
                return new HttpQuery(queryParameters);
            }
        };
    }

    private HttpQuery(final Iterable<QueryParameter> queryParameters) {
        super(unmodifiableList(convertSingleElementListOfValuelessQueryParameterNamedEmptyStringToEmptyList(requireNonNullElements(queryParameters))), HTTP_QUERY_PERCENT_ENCODING_PARTIAL);
    }

    private static List<QueryParameter> requireNonNullElements(final Iterable<QueryParameter> queryParameters) {
        final List<QueryParameter> result = new ArrayList<>();
        for (final QueryParameter queryParameter : queryParameters) {
            result.add(requireNonNull(queryParameter, "Cannot instantiate QueryParameters with null queryParameter"));
        }
        return result;
    }

    private static List<QueryParameter> convertSingleElementListOfValuelessQueryParameterNamedEmptyStringToEmptyList(final List<QueryParameter> queryParameters) {
        return queryParameters.size() == 1 && VALUELESS_EMPTY_NAMED_QUERY_PARAMETER.equals(queryParameters.get(0)) ? emptyList() : queryParameters;
    }

    private static <T> PercentEncodingPartial<Iterable<QueryParameter>, T> encodeQueryParameters(final PercentEncodingPartial<Iterable<Iterable<QueryParameter>>, T> childPercentEncodingPartial) {
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<Iterable<QueryParameter>, Iterable<Iterable<QueryParameter>>>() {
            @Override
            public Iterable<Iterable<QueryParameter>> encode(final Iterable<QueryParameter> queryParameters) {
                final List<Iterable<QueryParameter>> result = new ArrayList<>();
                for (final QueryParameter queryParameter : queryParameters) {
                    result.add(singletonList(queryParameter));
                }
                return result;
            }

            @Override
            public Iterable<QueryParameter> decode(final Iterable<Iterable<QueryParameter>> iterables) {
                final List<QueryParameter> result = new ArrayList<>();
                for (final Iterable<QueryParameter> queryParameters : iterables) {
                    for (final QueryParameter queryParameter : queryParameters) {
                        result.add(queryParameter);
                    }
                }
                return result;
            }
        });
    }

    private static <T> PercentEncodingPartial<QueryParameter, T> percentEncodedQueryParameter(final PercentEncodingPartial<Iterable<String>, T> childPercentEncodingPartial) {
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<QueryParameter, Iterable<String>>() {
            @Override
            public Iterable<String> encode(final QueryParameter queryParameter) {
                return queryParameter.encoded();
            }

            @Override
            public QueryParameter decode(final Iterable<String> strings) throws ParseException {
                final Iterator<String> iterator = strings.iterator();
                if (!iterator.hasNext()) {
                    throw new ParseException("Invalid query parameter String [" + strings + "]");
                }
                final QueryParameter result;
                final String name = iterator.next();
                if (iterator.hasNext()) {
                    result = queryParameter(name, iterator.next());
                    if (iterator.hasNext()) {
                        throw new ParseException("Invalid query parameter - expected maximum of two elements in [" + strings + "]");
                    }
                } else {
                    result = queryParameter(name);
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

    @Override
    @SuppressWarnings("NullableProblems")
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
         * <p>
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
            this.name = requireNonNull(name, "Cannot instantiate QueryParameter with null name");
            this.value = requireNonNull(value, "Cannot instantiate QueryParameter with null value");
        }

        @Override
        Iterable<String> encoded() {
            return asList(name, value);
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
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            } else if (object == null || getClass() != object.getClass()) {
                return false;
            }

            final NameAndValueQueryParameter that = (NameAndValueQueryParameter) object;

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
            this.name = requireNonNull(name, "Cannot instantiate QueryParameter with null name");
        }

        @Override
        Iterable<String> encoded() {
            return singletonList(name);
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
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            } else if (object == null || getClass() != object.getClass()) {
                return false;
            }

            final NameOnlyQueryParameter that = (NameOnlyQueryParameter) object;

            return name.equals(that.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
