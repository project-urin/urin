/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.CharacterSetMembershipFunction.P_CHAR;
import static net.sourceforge.urin.PercentEncodable.percentEncodableSpecifiedValue;
import static net.sourceforge.urin.PercentEncodable.percentEncodableString;
import static net.sourceforge.urin.PercentEncoder.ENCODE_NOTHING;

public abstract class Segment extends PercentEncodedUnaryValue {
    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(P_CHAR);
    public static final Segment EMPTY = segment("");
    public static final Segment DOT = new Segment(percentEncodableString("."), ENCODE_NOTHING) {
    };
    public static final Segment DOT_DOT = new Segment(percentEncodableString(".."), ENCODE_NOTHING) {
    };

    private Segment(final PercentEncodable percentEncodable, final PercentEncoder percentEncoder) {
        super(percentEncodable, percentEncoder);
    }

    public static Segment segment(final String segment) {
        return new Segment(percentEncodableSpecifiedValue(
                "..",
                percentEncodableSpecifiedValue(
                        ".",
                        percentEncodableString(segment))
        ), PERCENT_ENCODER) {
        };
    }

    boolean containsColon() {
        return this.value.containsColon();
    }
}
