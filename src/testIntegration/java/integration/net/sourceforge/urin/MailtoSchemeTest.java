/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin;

import net.sourceforge.urin.*;
import net.sourceforge.urin.Scheme.GenericScheme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Map.entry;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PercentEncodingPartial.noOp;
import static net.sourceforge.urin.PercentEncodingPartial.percentEncodingDelimitedValue;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MailtoSchemeTest {

    private static <T> PercentEncodingPartial<Iterable<QueryParameter>, T> encodeQueryParameters(final PercentEncodingPartial<Iterable<Iterable<QueryParameter>>, T> childPercentEncodingPartial) {
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<>() {
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
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<>() {
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
                    result = new QueryParameter(name, iterator.next());
                    if (iterator.hasNext()) {
                        throw new ParseException("Invalid query parameter - expected exactly two elements in [" + strings + "]");
                    }
                } else {
                    throw new ParseException("Invalid query parameter - expected exactly two elements in [" + strings + "]");
                }
                return result;
            }
        });
    }

    @Test
    void canRoundTripAMailtoUri() throws ParseException {
        assertThat(Mailto.mailto(asList("mark@example.com", "elvis@example.com")).asString(), equalTo("mailto:mark@example.com,elvis@example.com"));
        assertThat(Mailto.parse("mailto:mark@example.com,elvis@example.com"), equalTo(Mailto.mailto(asList("mark@example.com", "elvis@example.com"))));
        assertThat(Mailto.parse("mailto:mark@example.com,elvis@example.com").addresses(), equalTo(asList("mark@example.com", "elvis@example.com")));
    }

    @Test
    void canCreateAMailtoUriWithSubject() throws ParseException {
        assertThat(Mailto.mailto(asList("mark@example.com", "elvis@example.com"), "Hello, World!").asString(), equalTo("mailto:mark@example.com,elvis@example.com?subject=Hello,%20World!"));
        assertThat(Mailto.parse("mailto:mark@example.com,elvis@example.com?subject=Hello,%20World!"), equalTo(Mailto.mailto(asList("mark@example.com", "elvis@example.com"), "Hello, World!")));
    }

    @ParameterizedTest
    @ArgumentsSource(Rfc6068ExamplesArgumentsProvider.class)
    void canMakeRfc6068Examples(final String stringRepresentation, final Mailto mailtoRepresentation) throws ParseException {
        assertThat("can generate " + stringRepresentation, mailtoRepresentation.asString(), equalTo(stringRepresentation));
        assertThat("can parse " + stringRepresentation, Mailto.parse(stringRepresentation), equalTo(mailtoRepresentation));
    }

    private static final class Mailto {
        private static final PercentEncodingPartial<Iterable<String>, String> ADDRESS_PERCENT_ENCODING_PARTIAL = percentEncodingDelimitedValue(',');
        private static final Scheme<Iterable<String>, MailtoQuery, Fragment<?>> SCHEME = new GenericScheme<>(
                "mailto",
                new MakingDecoder<>(ADDRESS_PERCENT_ENCODING_PARTIAL) {
                    @Override
                    protected Segment<Iterable<String>> makeOne(final Iterable<String> strings) {
                        return segment(strings, ADDRESS_PERCENT_ENCODING_PARTIAL);
                    }
                },
                new MakingDecoder<>(MailtoQuery.MAILTO_QUERY_PERCENT_ENCODING_PARTIAL) {
                    @Override
                    protected MailtoQuery makeOne(final Iterable<QueryParameter> queryParameters) {
                        return new MailtoQuery(queryParameters);
                    }
                },
                new MakingDecoder<>(noOp()) {
                    @Override
                    protected Fragment<String> makeOne(final String value) {
                        return fragment(value);
                    }
                }
        );

        private final Urin<Iterable<String>, MailtoQuery, Fragment<?>> urin;

        private Mailto(final Urin<Iterable<String>, MailtoQuery, Fragment<?>> urin) {
            this.urin = urin;
        }

        static Mailto parse(final String uri) throws ParseException {
            return new Mailto(SCHEME.parseUrin(uri));
        }

        static Mailto mailto(final List<String> addresses) {
            return new Mailto(SCHEME.urin(rootlessPath(segment(addresses, ADDRESS_PERCENT_ENCODING_PARTIAL))));
        }

        static Mailto mailto(final List<String> addresses, final String subject) {
            return new Mailto(SCHEME.urin(rootlessPath(segment(addresses, ADDRESS_PERCENT_ENCODING_PARTIAL)), new MailtoQuery(List.of(new QueryParameter("subject", subject)))));
        }

        Iterable<String> addresses() {
            return urin.path().segments().get(0).value();
        }

        String asString() {
            return urin.asString();
        }

        @Override
        public boolean equals(final Object that) {
            if (this == that) {
                return true;
            } else if (that == null || getClass() != that.getClass()) {
                return false;
            }

            final Mailto mailto = (Mailto) that;
            return urin.equals(mailto.urin);
        }

        @Override
        public int hashCode() {
            return urin.hashCode();
        }
    }

    private static final class MailtoQuery extends Query<Iterable<QueryParameter>> {
        private static final PercentEncodingPartial<Iterable<QueryParameter>, String> MAILTO_QUERY_PERCENT_ENCODING_PARTIAL = encodeQueryParameters(
                percentEncodingDelimitedValue(
                        '&',
                        percentEncodingDelimitedValue(
                                ';', // TODO does ; actually need encoding?
                                percentEncodedQueryParameter(percentEncodingDelimitedValue(
                                        '=')))));


        MailtoQuery(final Iterable<QueryParameter> queryParameters) {
            super(queryParameters, MAILTO_QUERY_PERCENT_ENCODING_PARTIAL);
        }
    }

    private static final class QueryParameter {

        private final String name;
        private final String value;

        public QueryParameter(final String name, final String value) {
            this.name = name;
            this.value = value;
        }

        public Iterable<String> encoded() {
            return asList(name, value);
        }

        @Override
        public boolean equals(final Object that) {
            if (this == that) {
                return true;
            } else if (that == null || getClass() != that.getClass()) {
                return false;
            }

            final QueryParameter thatQueryParameter = (QueryParameter) that;
            return name.equals(thatQueryParameter.name) && value.equals(thatQueryParameter.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }

    static final class Rfc6068ExamplesArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    entry("mailto:chris@example.com", Mailto.mailto(List.of("chris@example.com"))),
                    entry("mailto:infobot@example.com?subject=current-issue", Mailto.mailto(List.of("infobot@example.com"), "current-issue"))
            ).map( entry -> Arguments.arguments(entry.getKey(), entry.getValue()));
        }
    }

}
