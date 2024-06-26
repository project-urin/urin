/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.PortBuilder.aPortDifferentTo;
import static net.sourceforge.urin.UserInfoBuilder.aUserInfo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorityTest {
    @Test
    void makesAuthorityWithNoUserInfoOrPort() {
        final Host host = aHost();
        assertThat(authority(host).asString(), equalTo(host.asString()));
    }

    @Test
    void rejectsNullHostInFactory() {
        assertThrows(NullPointerException.class, () -> authority(null), "Null host should throw NullPointerException in factory");
    }

    @Test
    void anAuthorityWithNoUserInfoOrPortIsEqualToAnotherWithTheSameHost() {
        final Host host = aHost();
        assertThat(authority(host), equalTo(authority(host)));
        assertThat(authority(host).hashCode(), equalTo(authority(host).hashCode()));
    }

    @Test
    void anAuthorityWithNoUserInfoOrPortIsNotEqualToAnotherWithTheADifferentHost() {
        assertThat(authority(aHost()), not(equalTo(authority(aHost()))));
    }

    @Test
    void anAuthorityWithNoUserInfoOrPortReturnsHost() {
        final Host host = aHost();
        assertThat(authority(host).host(), equalTo(host));
    }

    @Test
    void anAuthorityWithNoUserInfoOrPortToStringIsCorrect() {
        final Host host = aHost();
        assertThat(authority(host).toString(), equalTo("Authority{host=" + host + "}"));
    }

    @Test
    void makesAuthorityWithNoPort() {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        assertThat(authority(userInfo, host).asString(), equalTo(userInfo.asString() + "@" + host.asString()));
    }

    @Test
    void rejectsNullInFactoryForAuthorityWithNoPort() {
        assertThrows(NullPointerException.class, () -> authority(null, aHost()), "Null userInfo should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> authority(aUserInfo(), null), "Null host should throw NullPointerException in factory");
    }

    @Test
    void anAuthorityWithNoPortIsEqualToAnotherWithTheSameHostAndUserInfo() {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        assertThat(authority(userInfo, host), equalTo(authority(userInfo, host)));
        assertThat(authority(userInfo, host).hashCode(), equalTo(authority(userInfo, host).hashCode()));
    }

    @Test
    void anAuthorityWithNoPortIsNotEqualToAnotherWithTheADifferentHostAndUserInfo() {
        assertThat(authority(aUserInfo(), aHost()), not(equalTo(authority(aUserInfo(), aHost()))));
    }

    @Test
    void anAuthorityWithNoPortReturnsHost() {
        final Host host = aHost();
        assertThat(authority(aUserInfo(), host).host(), equalTo(host));
    }

    @Test
    void anAuthorityWithNoPortToStringIsCorrect() {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        assertThat(authority(userInfo, host).toString(), equalTo("Authority{userInfo=" + userInfo + ", host=" + host + "}"));
    }

    @Test
    void makesAuthorityWithNoUserInfo() {
        final Host host = aHost();
        final Port port = aPort();
        assertThat(authority(host, port).asString(), equalTo(host.asString() + ":" + port.asString()));
    }

    @Test
    void rejectsNullInFactoryForAuthorityWithNoUserInfo() {
        assertThrows(NullPointerException.class, () -> authority(null, aPort()), "Null host should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> authority(aHost(), null), "Null port should throw NullPointerException in factory");
    }

    @Test
    void anAuthorityWithNoUserInfoIsEqualToAnotherWithTheSameHostAndPort() {
        final Host host = aHost();
        final Port port = aPort();
        assertThat(authority(host, port), equalTo(authority(host, port)));
        assertThat(authority(host, port).hashCode(), equalTo(authority(host, port).hashCode()));
    }

    @Test
    void anAuthorityWithNoUserInfoIsNotEqualToAnotherWithTheADifferentHostAndPort() {
        assertThat(authority(aHost(), aPort()), not(equalTo(authority(aHost(), aPort()))));
    }

    @Test
    void anAuthorityWithNoUserInfoReturnsHost() {
        final Host host = aHost();
        assertThat(authority(host, aPort()).host(), equalTo(host));
    }

    @Test
    void anAuthorityWithNoUserInfoToStringIsCorrect() {
        final Host host = aHost();
        final Port port = aPort();
        assertThat(authority(host, port).toString(), equalTo("Authority{host=" + host + ", port=" + port + "}"));
    }

    @Test
    void makesAuthorityWithAllOptionsSpecified() {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        final Port port = aPort();
        assertThat(authority(userInfo, host, port).asString(), equalTo(userInfo.asString() + "@" + host.asString() + ":" + port.asString()));
    }

    @Test
    void rejectsNullInFactoryForAuthorityWithAllOptions() {
        assertThrows(NullPointerException.class, () -> authority(null, aHost(), aPort()), "Null userInfo should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> authority(aUserInfo(), null, aPort()), "Null host should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> authority(aUserInfo(), aHost(), null), "Null port should throw NullPointerException in factory");
    }


    @Test
    void anAuthorityWithAllOptionsSpecifiedIsEqualToAnotherWithTheSameFields() {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        final Port port = aPort();
        assertThat(authority(userInfo, host, port), equalTo(authority(userInfo, host, port)));
        assertThat(authority(userInfo, host, port).hashCode(), equalTo(authority(userInfo, host, port).hashCode()));
    }

    @Test
    void anAuthorityWithAllOptionsSpecifiedIsNotEqualToAnotherWithTheADifferentHostUserInfoAndPort() {
        assertThat(authority(aUserInfo(), aHost(), aPort()), not(equalTo(authority(aUserInfo(), aHost(), aPort()))));
    }

    @Test
    void anAuthorityWithAllOptionsSpecifiedReturnsHost() {
        final Host host = aHost();
        assertThat(authority(aUserInfo(), host, aPort()).host(), equalTo(host));
    }

    @Test
    void anAuthorityWithAllOptionsSpecifiedToStringIsCorrect() {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        final Port port = aPort();
        assertThat(authority(userInfo, host, port).toString(), equalTo("Authority{userInfo=" + userInfo + ", host=" + host + ", port=" + port + "}"));
    }

    @Test
    void parsesAuthorityWithNoUserInfoOrPort() throws Exception {
        final Host host = aHost();
        assertThat(Authority.parse(host.asString()), equalTo(authority(host)));
    }

    @Test
    void parsesAuthorityWithNoPort() throws Exception {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        assertThat(Authority.parse(userInfo.asString() + "@" + host.asString()), equalTo(authority(userInfo, host)));
    }

    @Test
    void parsesAuthorityWithNoUserInfo() throws Exception {
        final Host host = aHost();
        final Port port = aPort();
        assertThat(Authority.parse(host.asString() + ":" + port.asString()), equalTo(authority(host, port)));
    }

    @Test
    void parsesAuthorityWithAllOptionsSpecified() throws Exception {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        final Port port = aPort();
        assertThat(Authority.parse(userInfo.asString() + "@" + host.asString() + ":" + port.asString()), equalTo(authority(userInfo, host, port)));
    }

    @Test
    void parsingAnInvalidAuthorityStringThrowsParseException() {
        assertThrows(ParseException.class, () -> Authority.parse("something invalid"), "In invalid authority should throw a parse exception");
    }


    @Test
    void parsingNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Authority.parse(null), "Null value should throw NullPointerException in parser");
    }


    @Test
    void authorityWithNoUserInfoAndNoPortIsReturnedUnmolestedFromRemovingPort() {
        final Authority authority = authority(aHost());
        assertThat(authority.removePort(aPort()), equalTo(authority));
    }

    @Test
    void authorityWithUserInfoAndNoPortIsReturnedUnmolestedFromRemovingPort() {
        final Authority authority = authority(aUserInfo(), aHost());
        assertThat(authority.removePort(aPort()), equalTo(authority));
    }

    @Test
    void authorityWithPortIsReturnedUnmolestedFromRemovingDifferentPort() {
        final Port port = aPort();
        final Authority authority = authority(aHost(), port);
        assertThat(authority.removePort(aPortDifferentTo(port)), equalTo(authority));
    }

    @Test
    void authorityWithPortCorrectlyRemovesThatPort() {
        final Port port = aPort();
        final Host host = aHost();
        final Authority authority = authority(host, port);
        assertThat(authority.removePort(port), equalTo(authority(host)));
    }

    @Test
    void authorityWithUserInfoAndPortIsReturnedUnmolestedFromRemovingDifferentPort() {
        final Port port = aPort();
        final Authority authority = authority(aUserInfo(), aHost(), port);
        assertThat(authority.removePort(aPortDifferentTo(port)), equalTo(authority));
    }

    @Test
    void authorityWithUserInfoAndPortCorrectlyRemovesThatPort() {
        final UserInfo userInfo = aUserInfo();
        final Host host = aHost();
        final Port port = aPort();
        final Authority authority = authority(userInfo, host, port);
        assertThat(authority.removePort(port), equalTo(authority(userInfo, host)));
    }
}
