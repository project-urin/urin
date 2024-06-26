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

import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.Segment.segment;

public final class SegmentBuilder {

    private static final RandomSupplierSwitcher<Segment<String>> RANDOM_STRING_SEGMENT_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            SegmentBuilder::aNonDotSegment,
            Segment::dot,
            Segment::dotDot
    );

    private SegmentBuilder() {
    }

    public static Segment<String> aNonDotSegment() {
        return segment(aString());
    }

    public static Segment<String> aSegment() {
        return RANDOM_STRING_SEGMENT_SUPPLIER_SWITCHER.get();
    }

}