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
 * A parser of encoded URI components.
 *
 * @param <NON_ENCODED> the class of the decoded objects produced.
 * @param <ENCODED>     the class of the encoded objects parsed.
 */
public interface Decoder<NON_ENCODED, ENCODED> {

    /**
     * Parses an encoded URI component.
     *
     * @param rawValue the encoded URI component to decode.
     * @return an object representing the decoded URI component.
     * @throws ParseException if the given {@code String} is not a valid URI component.
     */
    NON_ENCODED decode(ENCODED rawValue) throws ParseException;
}
