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

import static net.sourceforge.urin.CharacterSetMembershipFunction.P_CHAR;
import static net.sourceforge.urin.PercentEncoder.ENCODE_NOTHING;

/**
 * A segment of a URI's path.
 * <p/>
 * Note that the special segments "." and ".." are defined as constants.  Passing "." or ".." as an argument to the
 * factory method {@link #segment(String)} is not equivalent, as the argument to this method is a literal string, i.e.
 * subject to encoding where necessary.
 * <p/>
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 - Path</a>
 */
public abstract class Segment extends PercentEncodedUnaryValue {
    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(P_CHAR);
    private static final PercentDecoder PERCENT_DECODER = new PercentDecoder(P_CHAR);
    /**
     * An empty segment
     */
    public static final Segment EMPTY = segment("");

    /**
     * The segment ".", referring to the current location in the path name hierarchy,
     */
    public static final Segment DOT = new Segment(PercentEncodable.percentEncodableString("."), ENCODE_NOTHING) {
    };
    /**
     * The segment "..", referring to the parent location in the path name hierarchy,
     */
    public static final Segment DOT_DOT = new Segment(PercentEncodable.percentEncodableString(".."), ENCODE_NOTHING) {
    };

    private Segment(final PercentEncodable percentEncodable, final PercentEncoder percentEncoder) {
        super(percentEncodable, percentEncoder);
    }

    /**
     * Factory method for creating {@code Segment}s.
     *
     * @param segment any {@code String} to represent as a {@code Segment}.
     * @return a {@code Segment} representing the given {@code String}.
     */
    public static Segment segment(final String segment) {
        return new Segment(PercentEncodable.percentEncodableSpecifiedValue(
                "..",
                PercentEncodable.percentEncodableSpecifiedValue(
                        ".",
                        PercentEncodable.percentEncodableString(segment))
        ), PERCENT_ENCODER) {
        };
    }

    boolean containsColon() {
        return this.value.containsColon();
    }

    static Segment parse(final String encodedSegment) throws ParseException {
        if (".".equals(encodedSegment)) {
            return DOT;
        } else if ("..".equals(encodedSegment)) {
            return DOT_DOT;
        } else {
            return segment(PERCENT_DECODER.decode(encodedSegment));
        }
    }
}
