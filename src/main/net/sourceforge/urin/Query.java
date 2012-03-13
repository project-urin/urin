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
import static net.sourceforge.urin.PercentEncodable.percentEncodableString;

/**
 * A query component of a URI.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.4">RFC 3986 - Query</a>
 */
public final class Query extends PercentEncodedUnaryValue {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS);
    private static final PercentDecoder PERCENT_DECODER = new PercentDecoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS);

    private Query(final PercentEncodable value) {
        super(value, PERCENT_ENCODER);
    }

    /**
     * Factory method for creating <code>Query</code>s.
     *
     * @param query any <code>String</code> to represent as a <code>Query</code>.
     * @return a <code>Query</code> representing the given <code>String</code>.
     */
    public static Query query(final String query) {
        return new Query(percentEncodableString(query));
    }

    /**
     * Factory method for creating <code>Query</code>s with scheme specific percent encoding of characters beyond that specified for generic URI <code>Query</code>s.
     *
     * @param query a <code>PercentEncodable</code> specifying the query and the additional scheme specific percent encoding.
     * @return a <code>Query</code> representing the given <code>PercentEncodable</code> that will use the additional percent encoding specified when used in a URI.
     */
    public static Query query(final PercentEncodable query) {
        return new Query(query);
    }

    static Query parse(final String queryString) throws ParseException {
        return query(PERCENT_DECODER.decode(queryString));
    }
}
