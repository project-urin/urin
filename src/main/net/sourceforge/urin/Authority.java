/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

public abstract class Authority {

    private Authority() {
    }

    abstract String asString();

    public static Authority authority(final Host host) {
        return new AuthorityWithHost(host);
    }

    public static Authority authority(final UserInfo userInfo, final Host host) {
        return new AuthorityWithUserInfoAndHost(userInfo, host);
    }

    public static Authority authority(final Host host, final Port port) {
        return new AuthorityWithHostAndPort(host, port);
    }

    public static Authority authority(final UserInfo userInfo, final Host host, final Port port) {
        return new Authority() {
            @Override
            String asString() {
                return new StringBuilder(userInfo.asString())
                        .append('@')
                        .append(host.asString())
                        .append(':')
                        .append(port.asString())
                        .toString();
            }
        };
    }

    private static class AuthorityWithHost extends Authority {
        private final Host host;

        public AuthorityWithHost(final Host host) {
            this.host = host;
        }

        @Override
        String asString() {
            return host.asString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorityWithHost that = (AuthorityWithHost) o;
            return !(host != null ? !host.equals(that.host) : that.host != null);
        }

        @Override
        public int hashCode() {
            return host != null ? host.hashCode() : 0;
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
            this.userInfo = userInfo;
            this.host = host;
        }

        @Override
        String asString() {
            return new StringBuilder(userInfo.asString())
                    .append('@')
                    .append(host.asString())
                    .toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorityWithUserInfoAndHost that = (AuthorityWithUserInfoAndHost) o;

            return !(host != null ? !host.equals(that.host) : that.host != null)
                    && !(userInfo != null ? !userInfo.equals(that.userInfo) : that.userInfo != null);

        }

        @Override
        public int hashCode() {
            int result = userInfo != null ? userInfo.hashCode() : 0;
            result = 31 * result + (host != null ? host.hashCode() : 0);
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
            this.host = host;
            this.port = port;
        }

        @Override
        String asString() {
            return new StringBuilder(host.asString())
                    .append(':')
                    .append(port.asString())
                    .toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorityWithHostAndPort that = (AuthorityWithHostAndPort) o;
            return !(host != null ? !host.equals(that.host) : that.host != null)
                    && !(port != null ? !port.equals(that.port) : that.port != null);

        }

        @Override
        public int hashCode() {
            int result = host != null ? host.hashCode() : 0;
            result = 31 * result + (port != null ? port.hashCode() : 0);
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
}
