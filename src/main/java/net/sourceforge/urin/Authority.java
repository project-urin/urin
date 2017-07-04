/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An authority component of a URI.
 *
 * An authority is made up of a host and optional user information and port.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.2">RFC 3986 - Authority</a>
 */
public abstract class Authority {

    private static final Pattern AUTHORITY_PATTERN = Pattern.compile("^((.*)@)?(\\[.*\\]|[^:]*)?(:(.*))?");

    private Authority() {
    }

    abstract String asString();

    /**
     * Factory method for creating {@code Authority}s with just a host.
     *
     * @param host any {@code Host}.
     * @return an {@code Authority} representing the given {@code Host}.
     */
    public static Authority authority(final Host host) {
        return new AuthorityWithHost(host);
    }

    /**
     * Factory method for creating {@code Authority}s with user information and host.
     *
     * @param userInfo any {@code UserInfo}.
     * @param host     any {@code Host}.
     * @return an {@code Authority} representing the given {@code UserInfo} and {@code Host}.
     */
    public static Authority authority(final UserInfo userInfo, final Host host) {
        return new AuthorityWithUserInfoAndHost(userInfo, host);
    }

    /**
     * Factory method for creating {@code Authority}s with host and port.
     *
     * @param host any {@code Host}.
     * @param port any {@code Port}
     * @return an {@code Authority} representing the given {@code Host} and {@code Port}.
     */
    public static Authority authority(final Host host, final Port port) {
        return new AuthorityWithHostAndPort(host, port);
    }

    /**
     * Factory method for creating {@code Authority}s with user information, host, and port.
     *
     * @param userInfo any {@code UserInfo}.
     * @param host     any {@code Host}.
     * @param port     any {@code Port}
     * @return an {@code Authority} representing the given {@code UserInfo}, {@code Host} and {@code Port}
     */
    public static Authority authority(final UserInfo userInfo, final Host host, final Port port) {
        return new AuthorityWithUserInfoAndHostAndPort(userInfo, host, port);
    }

    /**
     * Parses the given {@code String} as an authority.
     *
     * @param authority a {@code String} that represents a URI.
     * @return an {@code Authority} representing the authority represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid authority.
     */
    public static Authority parse(final String authority) throws ParseException {
        Matcher matcher = AUTHORITY_PATTERN.matcher(authority);
        if (!matcher.matches()) {
            throw new ParseException("[" + authority + "] is not a valid authority");
        }
        final String userInfoString = matcher.group(2);
        final String hostString = matcher.group(3);
        final String port = matcher.group(5);
        final Host host = Host.parse(hostString);
        if (userInfoString == null) {
            if (port == null) {
                return authority(host);
            } else {
                return authority(host, Port.parse(port));
            }
        } else {
            UserInfo userInfo = UserInfo.parse(userInfoString);
            if (port == null) {
                return authority(userInfo, host);
            } else {
                return authority(userInfo, host, Port.parse(port));
            }
        }
    }

    abstract Authority removePort(final Port port);

    private static class AuthorityWithHost extends Authority {
        private final Host host;

        public AuthorityWithHost(final Host host) {
            if (host == null) {
                throw new NullPointerException("Cannot instantiate Authority with null host");
            }
            this.host = host;
        }

        @Override
        String asString() {
            return host.asString();
        }

        @Override
        Authority removePort(final Port port) {
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorityWithHost that = (AuthorityWithHost) o;
            return host.equals(that.host);
        }

        @Override
        public int hashCode() {
            return host.hashCode();
        }

        @Override
        public String toString() {
            return "Authority{" +
                    "host=" + host +
                    '}';
        }
    }

    private static final class AuthorityWithUserInfoAndHost extends Authority {
        private final UserInfo userInfo;
        private final Host host;

        AuthorityWithUserInfoAndHost(final UserInfo userInfo, final Host host) {
            if (userInfo == null) {
                throw new NullPointerException("Cannot instantiate Authority with null userInfo");
            }
            this.userInfo = userInfo;
            if (host == null) {
                throw new NullPointerException("Cannot instantiate Authority with null host");
            }
            this.host = host;
        }

        @Override
        String asString() {
            return userInfo.asString() + '@' + host.asString();
        }

        @Override
        Authority removePort(final Port port) {
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorityWithUserInfoAndHost that = (AuthorityWithUserInfoAndHost) o;

            return host.equals(that.host)
                    && userInfo.equals(that.userInfo);

        }

        @Override
        public int hashCode() {
            int result = userInfo.hashCode();
            result = 31 * result + host.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Authority{" +
                    "userInfo=" + userInfo +
                    ", host=" + host +
                    '}';
        }
    }

    private static final class AuthorityWithHostAndPort extends Authority {
        private final Host host;
        private final Port port;

        AuthorityWithHostAndPort(final Host host, final Port port) {
            if (host == null) {
                throw new NullPointerException("Cannot instantiate Authority with null host");
            }
            this.host = host;
            if (port == null) {
                throw new NullPointerException("Cannot instantiate Authority with null port");
            }
            this.port = port;
        }

        @Override
        String asString() {
            return host.asString() + ':' + port.asString();
        }

        @Override
        Authority removePort(final Port port) {
            return this.port.equals(port) ? authority(host) : this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorityWithHostAndPort that = (AuthorityWithHostAndPort) o;
            return host.equals(that.host)
                    && port.equals(that.port);
        }

        @Override
        public int hashCode() {
            int result = host.hashCode();
            result = 31 * result + port.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Authority{" +
                    "host=" + host +
                    ", port=" + port +
                    '}';
        }
    }

    private static class AuthorityWithUserInfoAndHostAndPort extends Authority {
        private final UserInfo userInfo;
        private final Host host;
        private final Port port;

        public AuthorityWithUserInfoAndHostAndPort(final UserInfo userInfo, final Host host, final Port port) {
            if (userInfo == null) {
                throw new NullPointerException("Cannot instantiate Authority with null userInfo");
            }
            this.userInfo = userInfo;
            if (host == null) {
                throw new NullPointerException("Cannot instantiate Authority with null host");
            }
            this.host = host;
            if (port == null) {
                throw new NullPointerException("Cannot instantiate Authority with null port");
            }
            this.port = port;
        }

        @Override
        String asString() {
            return userInfo.asString() + '@' + host.asString() + ':' + port.asString();
        }

        @Override
        Authority removePort(final Port port) {
            return this.port.equals(port) ? authority(userInfo, host) : this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorityWithUserInfoAndHostAndPort that = (AuthorityWithUserInfoAndHostAndPort) o;
            return host.equals(that.host)
                    && port.equals(that.port)
                    && userInfo.equals(that.userInfo);
        }

        @Override
        public int hashCode() {
            int result = userInfo.hashCode();
            result = 31 * result + host.hashCode();
            result = 31 * result + port.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Authority{" +
                    "userInfo=" + userInfo +
                    ", host=" + host +
                    ", port=" + port +
                    '}';
        }
    }
}
