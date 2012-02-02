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

import java.net.URI;
import java.util.ArrayList;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Scheme.scheme;

public abstract class Urin {

    private Urin() {
        // deliberately empty
    }

    public abstract String asString();

    public URI asUri() {
        return URI.create(asString());
    }

    public abstract Urin resolve(final RelativeReference relativeReference);

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
        return new UrinWithHierarchicalPart(scheme, hierarchicalPart);
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Fragment fragment) {
        return new UrinWithHierarchicalPartAndFragment(scheme, hierarchicalPart, fragment);
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
        return new UrinWithHierarchicalPartAndQuery(scheme, hierarchicalPart, query);
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
        return new UrinWithHierarchicalPartAndQueryAndFragment(scheme, hierarchicalPart, query, fragment);
    }

    public static Urin parse(final String uriString) throws ParseException {
        return parse(URI.create(uriString));
    }

    public static Urin parse(final URI uri) throws ParseException {
        String host = uri.getHost();
        String query = uri.getQuery();
        int port = uri.getPort();
        if (port == -1) {
            if (host == null && query == null) {
                return urin(
                        scheme(uri.getScheme()),
                        hierarchicalPart(toSegments(uri.getRawPath())));
            } else if (host != null && query == null) {
                return urin(
                        scheme(uri.getScheme()),
                        hierarchicalPart(authority(Host.parse(host)), toSegments(uri.getRawPath())));
            } else if (host == null) {
                return urin(
                        scheme(uri.getScheme()),
                        hierarchicalPart(toSegments(uri.getRawPath())),
                        Query.query(query));
            } else {
                return urin(
                        scheme(uri.getScheme()),
                        hierarchicalPart(authority(Host.parse(host)), toSegments(uri.getRawPath())),
                        Query.query(query));
            }
        } else {
            if (host == null && query == null) {
                throw new ParseException("Specifying port is invalid without a host");
            } else if (host != null && query == null) {
                return urin(
                        scheme(uri.getScheme()),
                        hierarchicalPart(authority(Host.parse(host), Port.port(port)), toSegments(uri.getRawPath())));
            } else if (host == null) {
                throw new ParseException("Specifying port is invalid without a host");
            } else {
                return urin(
                        scheme(uri.getScheme()),
                        hierarchicalPart(authority(Host.parse(host), Port.port(port)), toSegments(uri.getRawPath())),
                        Query.query(query));
            }
        }
    }

    private static AbsoluteSegments toSegments(final String rawPath) {
        return Segments.segments(new ArrayList<Segment>() {{
            boolean isFirst = true;
            for (String segmentString : rawPath.split("/")) {
                if (!isFirst) {
                    add(Segment.segment(segmentString));
                }
                isFirst = false;
            }
        }});
    }

    private static final class UrinWithHierarchicalPartAndQueryAndFragment extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Query query;
        private final Fragment fragment;

        UrinWithHierarchicalPartAndQueryAndFragment(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("Cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("Cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate Urin with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return new StringBuilder(scheme.asString())
                    .append(':')
                    .append(hierarchicalPart.asString())
                    .append('?')
                    .append(query.asString())
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        public Urin resolve(final RelativeReference relativeReference) {
            return relativeReference.resolve(scheme, hierarchicalPart, query, fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndQueryAndFragment that = (UrinWithHierarchicalPartAndQueryAndFragment) o;
            return fragment.equals(that.fragment)
                    && hierarchicalPart.equals(that.hierarchicalPart)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithHierarchicalPartAndQuery extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Query query;

        UrinWithHierarchicalPartAndQuery(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (query == null) {
                throw new NullPointerException("cannot instantiate Urin with null query");
            }
            this.query = query;
        }

        @Override
        public String asString() {
            return new StringBuilder(scheme.asString())
                    .append(':')
                    .append(hierarchicalPart.asString())
                    .append('?')
                    .append(query.asString())
                    .toString();
        }

        @Override
        public Urin resolve(final RelativeReference relativeReference) {
            return relativeReference.resolve(scheme, hierarchicalPart, query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndQuery that = (UrinWithHierarchicalPartAndQuery) o;
            return hierarchicalPart.equals(that.hierarchicalPart)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class UrinWithHierarchicalPartAndFragment extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Fragment fragment;

        UrinWithHierarchicalPartAndFragment(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (fragment == null) {
                throw new NullPointerException("cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return new StringBuilder(scheme.asString())
                    .append(':')
                    .append(hierarchicalPart.asString())
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        public Urin resolve(final RelativeReference relativeReference) {
            return relativeReference.resolve(scheme, hierarchicalPart);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndFragment that = (UrinWithHierarchicalPartAndFragment) o;
            return fragment.equals(that.fragment)
                    && hierarchicalPart.equals(that.hierarchicalPart)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static class UrinWithHierarchicalPart extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;

        public UrinWithHierarchicalPart(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
        }

        @Override
        public String asString() {
            return new StringBuilder(scheme.asString())
                    .append(':')
                    .append(hierarchicalPart.asString())
                    .toString();
        }

        @Override
        public Urin resolve(final RelativeReference relativeReference) {
            return relativeReference.resolve(scheme, hierarchicalPart);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPart that = (UrinWithHierarchicalPart) o;
            return hierarchicalPart.equals(that.hierarchicalPart)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    '}';
        }
    }
}
