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

import java.util.List;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PercentEncodingPartial.noOp;
import static net.sourceforge.urin.PercentEncodingPartial.percentEncodingDelimitedValue;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MailtoSchemeTest {

    @Test
    void canCreateAMailtoUri() throws ParseException {
        assertThat(Mailto.urin(asList("mark@example.com", "elvis@example.com")).asString(), equalTo("mailto:mark@example.com,elvis@example.com"));
        final Urin<Iterable<String>, Query<?>, Fragment<?>> actual = Mailto.parseMailto("mailto:mark@example.com,elvis@example.com");
        assertThat(actual, equalTo(Mailto.urin(asList("mark@example.com", "elvis@example.com"))));
    }

    private static final class Mailto {
        private static final PercentEncodingPartial<Iterable<String>, String> ADDRESS_PERCENT_ENCODING_PARTIAL = percentEncodingDelimitedValue(',');
        private static final MakingDecoder<Segment<Iterable<String>>, Iterable<String>, String> SEGMENT_MAKING_DECODER = new MakingDecoder<>(ADDRESS_PERCENT_ENCODING_PARTIAL) {
            @Override
            protected Segment<Iterable<String>> makeOne(final Iterable<String> strings) {
                return segment(strings, ADDRESS_PERCENT_ENCODING_PARTIAL);
            }
        };
        private static final MakingDecoder<Query<?>, String, String> QUERY_MAKING_DECODER = new MakingDecoder<>(noOp()) {
            @Override
            protected Query<String> makeOne(final String s) {
                return query(s);
            }
        };
        private static final MakingDecoder<Fragment<?>, String, String> FRAGMENT_MAKING_DECODER = new MakingDecoder<>(noOp()) {
            @Override
            protected Fragment<String> makeOne(final String s) {
                return fragment(s);
            }
        };
        private static final Scheme<Iterable<String>, Query<?>, Fragment<?>> SCHEME = new GenericScheme<>("mailto", SEGMENT_MAKING_DECODER, QUERY_MAKING_DECODER, FRAGMENT_MAKING_DECODER);

        static Urin<Iterable<String>, Query<?>, Fragment<?>> parseMailto(final String uri) throws ParseException {
            return SCHEME.parseUrin(uri);
        }

        static Urin<Iterable<String>, Query<?>, Fragment<?>> urin(final List<String> addresses) {
            return SCHEME.urin(rootlessPath(segment(addresses, ADDRESS_PERCENT_ENCODING_PARTIAL)));
        }
    }
}
