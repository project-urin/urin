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

import static net.sourceforge.urin.AbEmptyPath.absolutePath;
import static net.sourceforge.urin.AbEmptyPath.emptyPath;
import static net.sourceforge.urin.NonEmptySegmentBuilder.aNonEmptySegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;

public class PathBuilder {

    private static final Random RANDOM = new Random();

    public static PathRootlessAbsoluteOrEmpty aPath() {
        return new PathRootlessAbsoluteOrEmpty(aNonEmptySegment(), segments(RANDOM.nextInt(5)));
    }

    public static AbEmptyPath anAbsoluteOrEmptyPath() {
        final AbEmptyPath abEmptyPath;
        switch (new Random().nextInt(2)) {
            case 0:
                abEmptyPath = absolutePath(segments(RANDOM.nextInt(6)));
                break;
            case 1:
                abEmptyPath = emptyPath();
                break;
            default:
                throw new Defect("Attempted to switch on more cases than are defined");
        }
        return abEmptyPath;
    }

    private static Segment[] segments(final int numberOfSegments) {
        Segment[] tailSegments = new Segment[numberOfSegments];
        for (int i = 0; i < tailSegments.length; i++) {
            tailSegments[i] = aSegment();
        }
        return tailSegments;
    }
}
