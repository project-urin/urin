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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY;

/**
 * A hierarchical part component of a URI.
 * <p/>
 * A hierarchical part has an optional {@link Authority} component, and a mandatory {@link Path} component,
 * though that may implicitly be the empty path.  If an {@code Authority} is specified, the {@code Path} must be
 * absolute.
 * <p/>
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3">RFC 3986 - Syntax Components</a>
 * @deprecated
 */
public abstract class HierarchicalPart {

    private static final Pattern HIERARCHICAL_PART_REFERENCE_PATTERN = Pattern.compile("^(//([^/?#]*))?([^?#]*)");

    private HierarchicalPart() {
    }

    static HierarchicalPart parse(final String hierarchicalPartString) throws ParseException {
        final Matcher matcher = HIERARCHICAL_PART_REFERENCE_PATTERN.matcher(hierarchicalPartString);
        matcher.matches();
        final String authorityString = matcher.group(2);
        final String path = matcher.group(3);
        HierarchicalPart hierarchicalPart;
        if (authorityString == null) {
            if (path == null || "".equals(path)) {
                hierarchicalPart = hierarchicalPart();
            } else {
                hierarchicalPart = hierarchicalPart(!path.startsWith("/") ? Path.parseRootlessPath(path) : Path.parseParse(path));
            }
        } else {
            Authority authority = Authority.parse(authorityString);
            hierarchicalPart = (path == null || "".equals(path)) ? hierarchicalPart(authority) : hierarchicalPart(authority, Path.parseParse(path));
        }
        return hierarchicalPart;
    }

    abstract String asString();

    abstract Path path();

    /**
     * Factory method for creating {@code HierarchicalPart}s with just an empty path.
     *
     * @return a {@code HierarchicalPart} with an empty path.
     */
    public static HierarchicalPart hierarchicalPart() {
        return new HierarchicalPartNoAuthority(new EmptyPath());
    }

    /**
     * Factory method for creating {@code HierarchicalPart}s with an {@code Authority} and an empty path.
     *
     * @param authority any {@code Authority} to include in this {@code HierarchicalPart}.
     * @return a {@code HierarchicalPart} with the given {@code Authority} and an empty path.
     */
    public static HierarchicalPart hierarchicalPart(final Authority authority) {
        return new HierarchicalPartWithAuthority(authority, new EmptyPath());
    }

    /**
     * Factory method for creating {@code HierarchicalPart}s with a {@code Path} and no {@code Authority}.
     *
     * @param path any {@code Path} to use in this {@code HierarchicalPart}.
     * @return a {@code HierarchicalPart} with the given {@code Path} and no {@code Authority}.
     */
    public static HierarchicalPart hierarchicalPart(final Path path) {
        return new HierarchicalPartNoAuthority(path);
    }

    /**
     * Factory method for creating {@code HierarchicalPart}s with an {@code Authority} and {@code Path}.
     *
     * @param authority any {@code Authority} to include in this {@code HierarchicalPart}.
     * @param path      any {@code AbsolutePath} to use in this {@code HierarchicalPart}.
     * @return a {@code HierarchicalPart} with the given {@code Authority} and {@code Path}.
     */
    public static HierarchicalPart hierarchicalPart(final Authority authority, final AbsolutePath path) {
        return new HierarchicalPartWithAuthority(authority, path);
    }

    abstract HierarchicalPart resolve(final Path relativeReferencePath);

    abstract HierarchicalPart normalisePort(final Scheme scheme);

    final HierarchicalPart resolve(final Authority relativeReferenceAuthority, final Path relativeReferencePath) {
        return new HierarchicalPartWithAuthority(relativeReferenceAuthority, relativeReferencePath);
    }

    private static final class HierarchicalPartNoAuthority extends HierarchicalPart {
        private final Path path;

        HierarchicalPartNoAuthority(final Path path) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null path");
            }
            this.path = path;
        }

        @Override
        String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY);
        }

        @Override
        Path path() {
            return path;
        }

        @Override
        HierarchicalPart resolve(final Path relativeReferencePath) {
            return hierarchicalPart(relativeReferencePath.resolveRelativeTo(path));
        }

        @Override
        HierarchicalPart normalisePort(final Scheme scheme) {
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HierarchicalPartNoAuthority that = (HierarchicalPartNoAuthority) o;
            return path.equals(that.path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }

        @Override
        public String toString() {
            return "HierarchicalPart{" +
                    "path=" + path +
                    '}';
        }
    }

    private static final class HierarchicalPartWithAuthority extends HierarchicalPart {
        private final Authority authority;
        private final Path path;

        HierarchicalPartWithAuthority(final Authority authority, final Path path) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null path");
            }
            this.path = path;
        }

        @Override
        String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT);
        }

        @Override
        Path path() {
            return path;
        }

        @Override
        HierarchicalPart resolve(final Path relativeReferencePath) {
            return new HierarchicalPartWithAuthority(authority, relativeReferencePath.resolveRelativeTo(path));
        }

        @Override
        HierarchicalPart normalisePort(final Scheme scheme) {
            return new HierarchicalPartWithAuthority(scheme.normalise(authority), path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HierarchicalPartWithAuthority that = (HierarchicalPartWithAuthority) o;
            return authority.equals(that.authority)
                    && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "HierarchicalPart{" +
                    "authority=" + authority +
                    ", path=" + path +
                    '}';
        }
    }

}
