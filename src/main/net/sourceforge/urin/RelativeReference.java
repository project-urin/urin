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

/**
 * A relative reference.
 * <p/>
 * A relative reference has a mandatory relative part component, that is made up of an optional authority, and a
 * mandatory path, and optional query and fragment parts.  The mandatory path may implicitly be the empty path.
 *
 * @param <QUERY> The type of {@code Query} used by this relative reference.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-4.2">RFC 3986 - Relative Reference</a>
 */
public abstract class RelativeReference<QUERY extends Query> extends UrinReference<QUERY> {

    RelativeReference() {
    }

}
