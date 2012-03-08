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

interface ExceptionFactory<T extends Exception> {
    static final ExceptionFactory<IllegalArgumentException> ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY = new ExceptionFactory<IllegalArgumentException>() {
        public IllegalArgumentException makeException(final String message) {
            return new IllegalArgumentException(message);
        }
    };
    static final ExceptionFactory<ParseException> PARSE_EXCEPTION_EXCEPTION_FACTORY = new ExceptionFactory<ParseException>() {
        public ParseException makeException(final String message) {
            return new ParseException(message);
        }
    };

    T makeException(String message);
}