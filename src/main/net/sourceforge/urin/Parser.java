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
 * @param <PRODUCES> the class of the decoded objects produced.
 */
public interface Parser<PRODUCES> {

    /**
     * Parses an encoded URI component.
     *
     * @param rawValue the encoded URI component to parse.
     * @return an object representing the decoded URI component.
     * @throws ParseException if the given {@code String} is not a valid URI component.
     */
    PRODUCES parse(String rawValue) throws ParseException;
}
