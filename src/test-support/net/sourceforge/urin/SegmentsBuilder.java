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

import com.google.common.base.Supplier;

import java.util.Random;

import static net.sourceforge.urin.SegmentBuilder.aSegment;

public class SegmentsBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<Segments> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<Segments>(
            new Supplier<Segments>() {
                public Segments get() {
                    return anAbsoluteSegments();
                }
            },
            new Supplier<Segments>() {
                public Segments get() {
                    return aRootlessSegments();
                }
            }
    );
    private static final Random RANDOM = new Random();

    public static AbsoluteSegments anAbsoluteSegments() {
        int numberOfSegments = RANDOM.nextInt(4) + 1;
        Segment[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = aSegment();
        }
        return Segments.segments(tailSegments);
    }

    public static Segments aRootlessSegments() {
        int numberOfSegments = RANDOM.nextInt(4) + 1;
        Segment[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = aSegment();
        }
        return RootlessSegments.rootlessSegments(tailSegments);
    }

    public static Segments aSegments() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

}
