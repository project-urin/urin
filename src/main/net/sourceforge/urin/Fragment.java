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
 * A fragment component of a URI.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.5">RFC 3986 - Fragment</a>
 */
public final class Fragment extends PercentEncodedUnaryValue {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS);
    private static final PercentDecoder PERCENT_DECODER = new PercentDecoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS);

    private Fragment(final String fragment) {
        super(percentEncodableString(fragment), PERCENT_ENCODER);
    }

    /**
     * Factory method for creating <code>Fragment</code>s.
     *
     * @param fragment any <code>String</code> to represent as a <code>Fragment</code>.
     * @return a <code>Fragment</code> representing the given <code>String</code>.
     */
    public static Fragment fragment(final String fragment) {
        return new Fragment(fragment);
    }

    static Fragment parse(final String fragment) throws ParseException {
        return new Fragment(PERCENT_DECODER.decode(fragment));
    }
}
