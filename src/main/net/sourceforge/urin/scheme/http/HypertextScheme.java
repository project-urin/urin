/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.Port;
import net.sourceforge.urin.SchemeWithDefaultPort;

/**
 * Superclass for HTTP and HTTPS schemes.
 */
public abstract class HypertextScheme extends SchemeWithDefaultPort {
    HypertextScheme(final String name, final Port defaultPort) {
        super(name, defaultPort, HttpQuery.QUERY_PARSER);
    }

}