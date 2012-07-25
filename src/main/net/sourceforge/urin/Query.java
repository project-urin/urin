/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.CharacterSetMembershipFunction.QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS;

/**
 * A query component of a URI.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.4">RFC 3986 - Query</a>
 */
public class Query extends PercentEncodedUnaryValue {

    protected static final QueryParser BASE_QUERY_PARSER = new Query.QueryParser() {
        public Query parse(final String rawQuery) throws ParseException {
            return query(PERCENT_ENCODER.decode(rawQuery));
        }
    };

    protected static String percentDecode(final String percentEncoded) throws ParseException {
        return PERCENT_ENCODER.decode(percentEncoded);
    }

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS);

    /**
     * Constructor for subclasses of {@code Query} with scheme specific percent encoding of characters beyond that specified for generic URI {@code Query}s.
     *
     * @param value a {@code PercentEncodable} specifying the query and the additional scheme specific percent encoding.
     */
    protected Query(final PercentEncodable value) {
        super(value, PERCENT_ENCODER);
    }

    /**
     * Factory method for creating {@code Query}s.
     *
     * @param query any {@code String} to represent as a {@code Query}.
     * @return a {@code Query} representing the given {@code String}.
     */
    public static Query query(final String query) {
        return new Query(PercentEncodable.percentEncodableString(query));
    }

    protected static interface QueryParser {
        Query parse(String rawQuery) throws ParseException;
    }
}
