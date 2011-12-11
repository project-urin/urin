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

import java.net.URI;

import static net.sourceforge.urin.Segments.segments;

public abstract class RelativeReference {
    public static RelativeReference relativeReference(final Segments segments) {
        return new PathOnlyRelativeReference(segments);
    }

    public abstract String asString();

    public URI asUri() {
        return URI.create(asString());
    }

    public static RelativeReference relativeReference(final Segment... segments) {
        return relativeReference(segments(segments));
    }

    private static final class PathOnlyRelativeReference extends RelativeReference {
        private final Segments segments;

        public PathOnlyRelativeReference(final Segments segments) {
            this.segments = segments;
        }

        @Override
        public String asString() {
            return segments.asString();
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "segments=" + segments +
                    '}';
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PathOnlyRelativeReference that = (PathOnlyRelativeReference) o;

            return !(segments != null ? !segments.equals(that.segments) : that.segments != null);

        }

        @Override
        public int hashCode() {
            return segments != null ? segments.hashCode() : 0;
        }
    }
}
