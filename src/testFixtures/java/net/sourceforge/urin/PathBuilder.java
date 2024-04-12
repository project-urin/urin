/*
 * Copyright 2024 Mark Slater
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
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;

public final class PathBuilder {

    private static final RandomSupplierSwitcher<Path<String>> RANDOM_POLLUTED_PATH_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            PathBuilder::anAbsolutePath,
            PathBuilder::aRootlessPath
    );

    private static final RandomSupplierSwitcher<Path<String>> RANDOM_UNPOLLUTED_PATH_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            PathBuilder::anUnpollutedAbsolutePath,
            PathBuilder::anUnpollutedRootlessPath
    );
    private static final Random RANDOM = new Random();
    private static final RandomSupplierSwitcher<AbsolutePath<String>> ABSOLUTE_PATH_RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            () -> path(aNonDotSegment()), // TODO wha?!
            () -> path(aNonDotSegment(), aNonDotSegment()),
            () -> path(aNonDotSegment(), aNonDotSegment(), aNonDotSegment()),
            () -> path(aNonDotSegment(), aNonDotSegment(), aNonDotSegment(), aNonDotSegment()),
            () -> path(aNonDotSegment(), aNonDotSegment(), aNonDotSegment(), aNonDotSegment(), aNonDotSegment())
    );
    private static final RandomSupplierSwitcher<Path<String>> PATH_RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            () -> rootlessPath(aNonDotSegment()),
            () -> rootlessPath(aNonDotSegment(), aNonDotSegment()),
            () -> rootlessPath(aNonDotSegment(), aNonDotSegment(), aNonDotSegment()),
            () -> rootlessPath(aNonDotSegment(), aNonDotSegment(), aNonDotSegment(), aNonDotSegment()),
            () -> rootlessPath(aNonDotSegment(), aNonDotSegment(), aNonDotSegment(), aNonDotSegment(), aNonDotSegment())
    );

    private PathBuilder() {
    }

    public static AbsolutePath<String> anUnpollutedAbsolutePath() {
        final int numberOfSegments = RANDOM.nextInt(4) + 1;
        return path(Stream.generate(SegmentBuilder::aNonDotSegment).limit(numberOfSegments).collect(toList()));
    }

    public static AbsolutePath<String> anAbsolutePath() {
        return ABSOLUTE_PATH_RANDOM_SUPPLIER_SWITCHER.get();
    }

    public static Path<String> anUnpollutedRootlessPath() {
        final int numberOfSegments = RANDOM.nextInt(4) + 1;
        return rootlessPath(Stream.generate(SegmentBuilder::aNonDotSegment).limit(numberOfSegments).collect(toList()));
    }

    public static Path<String> aRootlessPath() {
        return PATH_RANDOM_SUPPLIER_SWITCHER.get();
    }

    public static Path<String> aPath() {
        return RANDOM_POLLUTED_PATH_SUPPLIER_SWITCHER.get();
    }

    public static Path<String> anUnpollutedPath() {
        return RANDOM_UNPOLLUTED_PATH_SUPPLIER_SWITCHER.get();
    }

}