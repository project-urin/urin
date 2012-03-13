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

public class PathBuilder {

    @SuppressWarnings({"unchecked"})
    private static final RandomSupplierSwitcher<Path> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<Path>(
            new Supplier<Path>() {
                public Path get() {
                    return anAbsolutePath();
                }
            },
            new Supplier<Path>() {
                public Path get() {
                    return aRootlessPath();
                }
            }
    );
    private static final Random RANDOM = new Random();

    public static AbsolutePath anAbsolutePath() {
        int numberOfSegments = RANDOM.nextInt(4) + 1;
        Segment[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = aSegment();
        }
        return Path.path(tailSegments);
    }

    public static Path aRootlessPath() {
        int numberOfSegments = RANDOM.nextInt(4) + 1;
        Segment[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = aSegment();
        }
        return RootlessPath.rootlessPath(tailSegments);
    }

    public static Path aPath() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }

}
