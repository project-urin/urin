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

public class PathBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<Path<String>> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<Path<String>>(
            new Supplier<Path<String>>() {
                public Path<String> get() {
                    return anAbsolutePath();
                }
            },
            new Supplier<Path<String>>() {
                public Path<String> get() {
                    return aRootlessPath();
                }
            }
    );
    private static final Random RANDOM = new Random();

    public static AbsolutePath<String> anAbsolutePath() {
        int numberOfSegments = RANDOM.nextInt(4);
        Segment<String>[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = SegmentBuilder.aNonDotSegment();
        }
        return Path.path(SegmentBuilder.aNonDotSegment(), tailSegments);
    }

    public static Path<String> aRootlessPath() {
        int numberOfSegments = RANDOM.nextInt(4);
        Segment<String>[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = SegmentBuilder.aNonDotSegment();
        }
        return RootlessPath.rootlessPath(SegmentBuilder.aNonDotSegment(), tailSegments);
    }

    public static Path<String> aPath() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

}
