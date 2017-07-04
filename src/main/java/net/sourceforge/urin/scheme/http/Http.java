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
 * Scheme for HTTP URIs.
 */
public final class Http extends HypertextScheme {

    /**
     * The HTTP scheme.
     */
    public static final Scheme<String, HttpQuery, Fragment<String>> HTTP = new Http();

    private Http() {
        super("http", port(80));
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host) {
        return HTTP.urin(authority(host), AbsolutePath.<String>path());
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port) {
        return HTTP.urin(authority(host, port), AbsolutePath.<String>path());
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority) {
        return HTTP.urin(authority, AbsolutePath.<String>path());
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final AbsolutePath<String> path) {
        return HTTP.urin(authority(host), path);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port, final AbsolutePath<String> path) {
        return HTTP.urin(authority(host, port), path);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority, final AbsolutePath<String> path) {
        return HTTP.urin(authority, path);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final AbsolutePath<String> path, final HttpQuery query) {
        return HTTP.urin(authority(host), path, query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port, final AbsolutePath<String> path, final HttpQuery query) {
        return HTTP.urin(authority(host, port), path, query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority, final AbsolutePath<String> path, final HttpQuery query) {
        return HTTP.urin(authority, path, query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final HttpQuery query) {
        return HTTP.urin(authority(host), AbsolutePath.<String>path(), query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port, final HttpQuery query) {
        return HTTP.urin(authority(host, port), AbsolutePath.<String>path(), query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority, final HttpQuery query) {
        return HTTP.urin(authority, AbsolutePath.<String>path(), query);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Fragment<String> fragment) {
        return HTTP.urin(authority(host), AbsolutePath.<String>path(), fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port, final Fragment<String> fragment) {
        return HTTP.urin(authority(host, port), AbsolutePath.<String>path(), fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority, final Fragment<String> fragment) {
        return HTTP.urin(authority, AbsolutePath.<String>path(), fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final AbsolutePath<String> path, final Fragment<String> fragment) {
        return HTTP.urin(authority(host), path, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port, final AbsolutePath<String> path, final Fragment<String> fragment) {
        return HTTP.urin(authority(host, port), path, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority, final AbsolutePath<String> path, final Fragment<String> fragment) {
        return HTTP.urin(authority, path, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final AbsolutePath<String> path, final HttpQuery query, final Fragment<String> fragment) {
        return HTTP.urin(authority(host), path, query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port, final AbsolutePath<String> path, final HttpQuery query, final Fragment<String> fragment) {
        return HTTP.urin(authority(host, port), path, query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority, final AbsolutePath<String> path, final HttpQuery query, final Fragment<String> fragment) {
        return HTTP.urin(authority, path, query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final HttpQuery query, final Fragment<String> fragment) {
        return HTTP.urin(authority(host), AbsolutePath.<String>path(), query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Host host, final Port port, final HttpQuery query, final Fragment<String> fragment) {
        return HTTP.urin(authority(host, port), AbsolutePath.<String>path(), query, fragment);
    }

    public static Urin<String, HttpQuery, Fragment<String>> http(final Authority authority, final HttpQuery query, final Fragment<String> fragment) {
        return HTTP.urin(authority, query, fragment);
    }

}
