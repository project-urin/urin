/*
 * Copyright 2013 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.CharacterSetMembershipFunction.QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS;
import static net.sourceforge.urin.PercentEncodingPartial.PercentEncoding.percentEncodingString;

/**
 * A query component of a URI.
 * Immutable and threadsafe.
 *
 * @param <ENCODES> The type of value represented by the query - {@code String} in the general case.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.4">RFC 3986 - Query</a>
 */
public class Query<ENCODES> extends PercentEncodingUnaryValue<ENCODES> {

    private static final PercentEncodingPartial.PercentEncoding<String> PERCENT_ENCODING = percentEncodingString(new PercentEncoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS));

    private Query(final ENCODES value, final PercentEncodingPartial.PercentEncoding<ENCODES> percentEncoding) {
        super(value, percentEncoding);
    }

    /**
     * Constructor for subclasses of {@code Query} with scheme specific percent encoding of characters beyond that specified for generic URI {@code Query}s.
     *
     * @param value                  the (non encoded) value this object represents.
     * @param percentEncodingPartial the {@code PercentEncodingPartial} this subclass will use.
     */
    protected Query(final ENCODES value, final PercentEncodingPartial<ENCODES, String> percentEncodingPartial) {
        this(value, percentEncodingPartial.apply(PERCENT_ENCODING));
    }

    /**
     * Factory method for creating {@code Query}s.
     *
     * @param query any {@code String} to represent as a {@code Query}.
     * @return a {@code Query} representing the given {@code String}.
     */
    public static Query<String> query(final String query) {
        return new Query<>(query, PERCENT_ENCODING);
    }

    public static MakingDecoder<Query<String>, String, String> stringQueryMaker() {
        return new MakingDecoder<Query<String>, String, String>(PercentEncodingPartial.<String>noOp()) {
            @Override
            protected Query<String> makeOne(String value) {
                return query(value);
            }
        };
    }

    static <QUERY extends Query> QUERY parseQuery(String queryString, MakingDecoder<QUERY, ?, String> queryMakingDecoder) throws ParseException {
        return queryMakingDecoder.toMaker(PERCENT_ENCODING).make(queryString);
    }

    /**
     * Gets the (non-encoded) value of this query.
     *
     * @return the (non-encoded) value of this query.
     */
    public final ENCODES value() {
        return value;
    }
}
