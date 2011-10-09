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

    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
        return new Urin() {
            @Override
            public String asString() {
                return new StringBuilder(scheme.asString())
                        .append(":")
                        .append(hierarchicalPart.asString())
                        .toString();
            }
        };
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart path, final Fragment fragment) {
        return new Urin() {
            @Override
            public String asString() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart path, final Query query) {
        return new Urin() {
            @Override
            public String asString() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    public static Urin urin(final Scheme scheme, final HierarchicalPart path, final Query query, final Fragment fragment) {
        return new Urin() {
            @Override
            public String asString() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    private Urin() {
        // deliberately empty
    }

    public abstract String asString();

    public URI asUri() {
        return URI.create(asString());
    }
}
