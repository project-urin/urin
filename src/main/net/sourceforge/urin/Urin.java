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

public abstract class Urin {

    private Urin() {
        // deliberately empty
    }

    public abstract String asString();

    public URI asUri() {
        return URI.create(asString());
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
        return new Urin() {
            @Override
            public String asString() {
                return new StringBuilder(scheme.asString())
                        .append(':')
                        .append(hierarchicalPart.asString())
                        .toString();
            }
        };
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Fragment fragment) {
        return new Urin() {
            @Override
            public String asString() {
                return new StringBuilder(scheme.asString())
                        .append(':')
                        .append(hierarchicalPart.asString())
                        .append('#')
                        .append(fragment.asString())
                        .toString();
            }
        };
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
        return new Urin() {
            @Override
            public String asString() {
                return new StringBuilder(scheme.asString())
                        .append(':')
                        .append(hierarchicalPart.asString())
                        .append('?')
                        .append(query.asString())
                        .toString();
            }
        };
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
        return new UrinWithSchemeAndHierarchicalPartAndQueryAndFragment(scheme, hierarchicalPart, query, fragment);
    }

    private static final class UrinWithSchemeAndHierarchicalPartAndQueryAndFragment extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Query query;
        private final Fragment fragment;

        UrinWithSchemeAndHierarchicalPartAndQueryAndFragment(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            this.scheme = scheme;
            this.hierarchicalPart = hierarchicalPart;
            this.query = query;
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
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithSchemeAndHierarchicalPartAndQueryAndFragment that = (UrinWithSchemeAndHierarchicalPartAndQueryAndFragment) o;
            return !(fragment != null ? !fragment.equals(that.fragment) : that.fragment != null)
                    && !(hierarchicalPart != null ? !hierarchicalPart.equals(that.hierarchicalPart) : that.hierarchicalPart != null)
                    && !(query != null ? !query.equals(that.query) : that.query != null)
                    && !(scheme != null ? !scheme.equals(that.scheme) : that.scheme != null);
        }

        @Override
        public int hashCode() {
            int result = scheme != null ? scheme.hashCode() : 0;
            result = 31 * result + (hierarchicalPart != null ? hierarchicalPart.hashCode() : 0);
            result = 31 * result + (query != null ? query.hashCode() : 0);
            result = 31 * result + (fragment != null ? fragment.hashCode() : 0);
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
}
