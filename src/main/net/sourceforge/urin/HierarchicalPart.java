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

public abstract class HierarchicalPart {

    private HierarchicalPart() {
    }

    abstract String asString();

    public static HierarchicalPart hierarchicalPart(final PathRootlessAbsoluteOrEmpty pathRootlessAbsoluteOrEmpty) {
        return new HierarchicalPart() {
            @Override
            String asString() {
                return pathRootlessAbsoluteOrEmpty.asString();
            }
        };
    }

    public static HierarchicalPart hierarchicalPart(final Authority authority) {
        return new HierarchicalPart() {
            @Override
            String asString() {
                return new StringBuilder("//")
                        .append(authority.asString())
                        .toString();
            }
        };
    }

    public static HierarchicalPart hierarchicalPart(final Authority authority, final AbEmptyPath path) {
        return new HierarchicalPart() {
            @Override
            String asString() {
                return new StringBuilder("//")
                        .append(authority.asString())
                        .append(path.asString())
                        .toString();
            }
        };
    }
}
