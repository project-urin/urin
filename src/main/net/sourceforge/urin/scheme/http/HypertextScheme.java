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

import net.sourceforge.urin.*;

import java.net.URI;

abstract class HypertextScheme extends SchemeWithDefaultPort<String, HttpQuery> {
    HypertextScheme(final String name, final Port defaultPort) {
        super(name, defaultPort, Segment.BASE_SEGMENT_DECODER, HttpQuery.QUERY_DECODER);
    }

    public static Urin<String, HttpQuery> parseHttpUrin(final URI uri) throws ParseException {
        return Http.HTTP.parseUrin(uri);
    }

    public static Urin<String, HttpQuery> parseHttpUrin(final String uri) throws ParseException {
        return Http.HTTP.parseUrin(uri);
    }

    public static RelativeReference<String, HttpQuery> parseHttpRelativeReference(final URI uri) throws ParseException {
        return Http.HTTP.parseRelativeReference(uri);
    }

    public static RelativeReference<String, HttpQuery> parseHttpRelativeReference(final String uri) throws ParseException {
        return Http.HTTP.parseRelativeReference(uri);
    }

    public static UrinReference<String, HttpQuery> parseHttpUrinReference(final URI uri) throws ParseException {
        return Http.HTTP.parseUrinReference(uri);
    }

    public static UrinReference<String, HttpQuery> parseHttpUrinReference(final String uri) throws ParseException {
        return Http.HTTP.parseUrinReference(uri);
    }
}
