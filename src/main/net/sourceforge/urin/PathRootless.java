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

public class PathRootless {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(P_CHAR);

    private final String firstSegment;
    private final String[] segments;

    public PathRootless(final String firstSegment, final String... segments) {
        this.firstSegment = firstSegment;
        this.segments = segments.clone();
    }

    public String asString() {
        StringBuilder result = new StringBuilder(PERCENT_ENCODER.encode(firstSegment));
        for (String pathSegment : segments) {
            result.append("/").append(PERCENT_ENCODER.encode(pathSegment));
        }
        return result.toString();
    }

}
