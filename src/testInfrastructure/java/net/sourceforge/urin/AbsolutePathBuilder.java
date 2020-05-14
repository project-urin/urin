/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class AbsolutePathBuilder {

    private static final Random RANDOM = new Random();

    private AbsolutePathBuilder() {
    }

    public static AbsolutePath<String> anAbsolutePath() {
        final int segmentCount = RANDOM.nextInt(5);
        return Path.path(Stream.generate(SegmentBuilder::aSegment).limit(segmentCount).collect(toList()));
    }
}
