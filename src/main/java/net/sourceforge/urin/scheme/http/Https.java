/*
 * Copyright 2017 Mark Slater
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
import static net.sourceforge.urin.Port.port;

/**
 * Scheme for HTTPS URIs.
 */
public final class Https extends HypertextScheme {

    /**
     * The HTTPS scheme.
     */
    public static final Scheme<String, HttpQuery, Fragment<String>> HTTPS = new Https();

    private Https() {
        super("https", port(443));
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host) {
        return HTTPS.urin(authority(host), AbsolutePath.<String>path());
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port) {
        return HTTPS.urin(authority(host, port), AbsolutePath.<String>path());
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority) {
        return HTTPS.urin(authority, AbsolutePath.<String>path());
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final AbsolutePath<String> path) {
        return HTTPS.urin(authority(host), path);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port, final AbsolutePath<String> path) {
        return HTTPS.urin(authority(host, port), path);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority, final AbsolutePath<String> path) {
        return HTTPS.urin(authority, path);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final AbsolutePath<String> path, final HttpQuery query) {
        return HTTPS.urin(authority(host), path, query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port, final AbsolutePath<String> path, final HttpQuery query) {
        return HTTPS.urin(authority(host, port), path, query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority, final AbsolutePath<String> path, final HttpQuery query) {
        return HTTPS.urin(authority, path, query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final HttpQuery query) {
        return HTTPS.urin(authority(host), AbsolutePath.<String>path(), query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port, final HttpQuery query) {
        return HTTPS.urin(authority(host, port), AbsolutePath.<String>path(), query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority, final HttpQuery query) {
        return HTTPS.urin(authority, AbsolutePath.<String>path(), query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host), AbsolutePath.<String>path(), fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host, port), AbsolutePath.<String>path(), fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority, final Fragment<String> fragment) {
        return HTTPS.urin(authority, AbsolutePath.<String>path(), fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final AbsolutePath<String> path, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host), path, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port, final AbsolutePath<String> path, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host, port), path, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority, final AbsolutePath<String> path, final Fragment<String> fragment) {
        return HTTPS.urin(authority, path, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final AbsolutePath<String> path, final HttpQuery query, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host), path, query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port, final AbsolutePath<String> path, final HttpQuery query, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host, port), path, query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority, final AbsolutePath<String> path, final HttpQuery query, final Fragment<String> fragment) {
        return HTTPS.urin(authority, path, query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final HttpQuery query, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host), AbsolutePath.<String>path(), query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Host host, final Port port, final HttpQuery query, final Fragment<String> fragment) {
        return HTTPS.urin(authority(host, port), AbsolutePath.<String>path(), query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> https(final Authority authority, final HttpQuery query, final Fragment<String> fragment) {
        return HTTPS.urin(authority, AbsolutePath.<String>path(), query, fragment);
    }
}
