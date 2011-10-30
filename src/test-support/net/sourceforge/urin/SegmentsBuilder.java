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

import java.util.Random;

import static net.sourceforge.urin.SegmentBuilder.aSegment;

public class SegmentsBuilder {

    private static final Random RANDOM = new Random();

    public static Segments aSegments() {
        Segment[] segments = new Segment[RANDOM.nextInt(5)];
        for (int i = 0; i < segments.length; i++) {
            segments[i] = aSegment();
        }
        return Segments.segments(segments);
    }
}
