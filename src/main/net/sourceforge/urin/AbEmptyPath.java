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

public abstract class AbEmptyPath {
    public static AbEmptyPath emptyPath() {
        return new AbEmptyPath() {
            @Override
            public String asString() {
                return "";
            }
        };
    }

    private AbEmptyPath() {
    }

    public abstract String asString();

    public static AbEmptyPath absolutePath() {
        return new AbEmptyPath() {
            @Override
            public String asString() {
                return "/";
            }
        };
    }

    public static AbEmptyPath absolutePath(final Segment... segments) {
        return new AbEmptyPath() {
            @Override
            public String asString() {
                StringBuilder stringBuilder = new StringBuilder();
                for (Segment segment : segments) {
                    stringBuilder
                            .append('/')
                            .append(segment.asString());
                }
                return stringBuilder.toString();
            }
        };
    }
}