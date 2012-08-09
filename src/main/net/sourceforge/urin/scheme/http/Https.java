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

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Port.port;

/**
 * Scheme for HTTPS URIs.
 */
public final class Https extends HypertextScheme {

    /**
     * The HTTPS scheme.
     */
    public static final Scheme<HttpQuery> HTTPS = new Https();

    private Https() {
        super("https", port(443));
    }

    public static Urin<HttpQuery> https(final Host host) {
        return HTTPS.urin(authority(host), path());
    }

    public static Urin<HttpQuery> https(final Host host, final Port port) {
        return HTTPS.urin(authority(host, port), path());
    }

    public static Urin<HttpQuery> https(final Authority authority) {
        return HTTPS.urin(authority, path());
    }

    public static Urin<HttpQuery> https(final Host host, final AbsolutePath path) {
        return HTTPS.urin(authority(host), path);
    }

    public static Urin<HttpQuery> https(final Host host, final Port port, final AbsolutePath path) {
        return HTTPS.urin(authority(host, port), path);
    }

    public static Urin<HttpQuery> https(final Authority authority, final AbsolutePath path) {
        return HTTPS.urin(authority, path);
    }

    public static Urin<HttpQuery> https(final Host host, final AbsolutePath path, final HttpQuery query) {
        return HTTPS.urin(authority(host), path, query);
    }

    public static Urin<HttpQuery> https(final Host host, final Port port, final AbsolutePath path, final HttpQuery query) {
        return HTTPS.urin(authority(host, port), path, query);
    }

    public static Urin<HttpQuery> https(final Authority authority, final AbsolutePath path, final HttpQuery query) {
        return HTTPS.urin(authority, path, query);
    }

    public static Urin<HttpQuery> https(final Host host, final HttpQuery query) {
        return HTTPS.urin(authority(host), path(), query);
    }

    public static Urin<HttpQuery> https(final Host host, final Port port, final HttpQuery query) {
        return HTTPS.urin(authority(host, port), path(), query);
    }

    public static Urin<HttpQuery> https(final Authority authority, final HttpQuery query) {
        return HTTPS.urin(authority, path(), query);
    }

    public static Urin<HttpQuery> https(final Host host, final Fragment fragment) {
        return HTTPS.urin(authority(host), path(), fragment);
    }

    public static Urin<HttpQuery> https(final Host host, final Port port, final Fragment fragment) {
        return HTTPS.urin(authority(host, port), path(), fragment);
    }

    public static Urin<HttpQuery> https(final Authority authority, final Fragment fragment) {
        return HTTPS.urin(authority, path(), fragment);
    }

    public static Urin<HttpQuery> https(final Host host, final AbsolutePath path, final Fragment fragment) {
        return HTTPS.urin(authority(host), path, fragment);
    }

    public static Urin<HttpQuery> https(final Host host, final Port port, final AbsolutePath path, final Fragment fragment) {
        return HTTPS.urin(authority(host, port), path, fragment);
    }

    public static Urin<HttpQuery> https(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return HTTPS.urin(authority, path, fragment);
    }

    public static Urin<HttpQuery> https(final Host host, final AbsolutePath path, final HttpQuery query, final Fragment fragment) {
        return HTTPS.urin(authority(host), path, query, fragment);
    }

    public static Urin<HttpQuery> https(final Host host, final Port port, final AbsolutePath path, final HttpQuery query, final Fragment fragment) {
        return HTTPS.urin(authority(host, port), path, query, fragment);
    }

    public static Urin<HttpQuery> https(final Authority authority, final AbsolutePath path, final HttpQuery query, final Fragment fragment) {
        return HTTPS.urin(authority, path, query, fragment);
    }

    public static Urin<HttpQuery> https(final Host host, final HttpQuery query, final Fragment fragment) {
        return HTTPS.urin(authority(host), path(), query, fragment);
    }

    public static Urin<HttpQuery> https(final Host host, final Port port, final HttpQuery query, final Fragment fragment) {
        return HTTPS.urin(authority(host, port), path(), query, fragment);
    }

    public static Urin<HttpQuery> https(final Authority authority, final HttpQuery query, final Fragment fragment) {
        return HTTPS.urin(authority, path(), query, fragment);
    }
}
