/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.CharacterSetMembershipFunction.*;

/**
 * A user information component of a URI.
 * Immutable and thread safe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.2.1">RFC 3986 - User Information</a>
 */
public final class UserInfo extends PercentEncodingUnaryValue<String> {

    private static final PercentEncodingPartial.PercentEncoding<String> PERCENT_ENCODING = PercentEncodingPartial.PercentEncoding.percentEncodingString(new PercentEncoder(or(
            UNRESERVED,
            SUB_DELIMITERS,
            singleMemberCharacterSet(':')
    )));

    private UserInfo(final String userInfo) {
        super(userInfo, PERCENT_ENCODING);
    }

    /**
     * Factory method for creating {@code UserInfo}s.
     *
     * @param userInfo any {@code String} to represent as a {@code UserInfo}.
     * @return a {@code UserInfo} representing the given {@code String}.
     */
    public static UserInfo userInfo(final String userInfo) {
        return new UserInfo(userInfo);
    }

    static UserInfo parse(final String userInfoString) throws ParseException {
        return userInfo(PERCENT_ENCODING.decode(userInfoString));
    }
}
