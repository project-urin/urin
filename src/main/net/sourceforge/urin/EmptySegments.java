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

final class EmptySegments extends Segments {

    EmptySegments() {
    }

    boolean firstPartIsSuppliedButIsEmpty() {
        return false;
    }

    boolean isEmpty() {
        return true;
    }

    @Override
    boolean firstPartIsSuppliedButContainsColon() {
        return false;
    }

    @Override
    Segments resolveRelativeTo(final Segments baseSegments) {
        return baseSegments;
    }

    @Override
    Segments replaceLastSegmentWith(final Iterable<Segment> segments) {
        return rootlessSegments(segments);
    }

    String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria) {
        return "";
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public String toString() {
        return "EmptySegments";
    }
}
