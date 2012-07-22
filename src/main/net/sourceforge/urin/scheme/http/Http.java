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
 * Utility class providing factory methods for generating HTTP and HTTPS URIs.
 */
public final class Http extends HypertextScheme {

    public static final Scheme HTTP = new Http();

    private Http() {
        super("http", port(80));
    }

    public static Urin http(final Host host) {
        return HTTP.urin(authority(host), path());
    }

    public static Urin http(final Host host, final Port port) {
        return HTTP.urin(authority(host, port), path());
    }

    public static Urin http(final Authority authority) {
        return HTTP.urin(authority, path());
    }

    public static Urin http(final Host host, final AbsolutePath path) {
        return HTTP.urin(authority(host), path);
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path) {
        return HTTP.urin(authority(host, port), path);
    }

    public static Urin http(final Authority authority, final AbsolutePath path) {
        return HTTP.urin(authority, path);
    }

    public static Urin http(final Host host, final AbsolutePath path, final Query query) {
        return HTTP.urin(authority(host), path, query);
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path, final Query query) {
        return HTTP.urin(authority(host, port), path, query);
    }

    public static Urin http(final Authority authority, final AbsolutePath path, final Query query) {
        return HTTP.urin(authority, path, query);
    }

    public static Urin http(final Host host, final Query query) {
        return HTTP.urin(authority(host), path(), query);
    }

    public static Urin http(final Host host, final Port port, final Query query) {
        return HTTP.urin(authority(host, port), path(), query);
    }

    public static Urin http(final Authority authority, final Query query) {
        return HTTP.urin(authority, path(), query);
    }

    public static Urin http(final Host host, final Fragment fragment) {
        return HTTP.urin(authority(host), path(), fragment);
    }

    public static Urin http(final Host host, final Port port, final Fragment fragment) {
        return HTTP.urin(authority(host, port), path(), fragment);
    }

    public static Urin http(final Authority authority, final Fragment fragment) {
        return HTTP.urin(authority, path(), fragment);
    }

    public static Urin http(final Host host, final AbsolutePath path, final Fragment fragment) {
        return HTTP.urin(authority(host), path, fragment);
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path, final Fragment fragment) {
        return HTTP.urin(authority(host, port), path, fragment);
    }

    public static Urin http(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return HTTP.urin(authority, path, fragment);
    }

    public static Urin http(final Host host, final AbsolutePath path, final Query query, final Fragment fragment) {
        return HTTP.urin(authority(host), path, query, fragment);
    }

    public static Urin http(final Host host, final Port port, final AbsolutePath path, final Query query, final Fragment fragment) {
        return HTTP.urin(authority(host, port), path, query, fragment);
    }

    public static Urin http(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return HTTP.urin(authority, path, query, fragment);
    }

    public static Urin http(final Host host, final Query query, final Fragment fragment) {
        return HTTP.urin(authority(host), path(), query, fragment);
    }

    public static Urin http(final Host host, final Port port, final Query query, final Fragment fragment) {
        return HTTP.urin(authority(host, port), path(), query, fragment);
    }

    public static Urin http(final Authority authority, final Query query, final Fragment fragment) {
        return HTTP.urin(authority, path(), query, fragment);
    }

}
