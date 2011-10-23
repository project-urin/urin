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
        return new Authority() {
            @Override
            String asString() {
                return host.asString();
            }
        };
    }

    public static Authority authority(final UserInfo userInfo, final Host host) {
        return new Authority() {
            @Override
            String asString() {
                return new StringBuilder(userInfo.asString())
                        .append('@')
                        .append(host.asString())
                        .toString();
            }
        };
    }

    public static Authority authority(final Host host, final Port port) {
        return new Authority() {
            @Override
            String asString() {
                return new StringBuilder(host.asString())
                        .append(':')
                        .append(port.asString())
                        .toString();
            }
        };
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

}
