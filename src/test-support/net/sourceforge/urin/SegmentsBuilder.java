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

import java.util.Random;

import static net.sourceforge.urin.SegmentBuilder.aSegment;

public class SegmentsBuilder {
    private static final Random RANDOM = new Random();

    public static AbsoluteSegments absoluteSegments() {
        int numberOfSegments = RANDOM.nextInt(5);
        Segment[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = aSegment();
        }
        return Segments.segments(tailSegments);
    }

    public static Segments relativeSegments() {
        int numberOfSegments = RANDOM.nextInt(5);
        Segment[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = aSegment();
        }
        return RelativeSegments.rootlessSegments(tailSegments);
    }

    public static Segments segments() {
        final Segments segments;
        switch (RANDOM.nextInt(2)) {
            case 0:
                segments = absoluteSegments();
                break;
            case 1:
                segments = relativeSegments();
                break;
            default:
                throw new Defect("Attempted to switch on more cases than are defined");
        }
        return segments;
    }
}
