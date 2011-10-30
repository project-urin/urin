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

import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.UserInfoBuilder.aUserInfo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class AuthorityTest {
    @Test
    public void makesAuthorityWithNoUserInfoOrPort() throws Exception {
        Host host = aHost();
        assertThat(authority(host).asString(), equalTo(host.asString()));
    }

    @Test
    public void anAuthorityWithNoUserInfoOrPortIsEqualToAnotherWithTheSameHost() throws Exception {
        Host host = aHost();
        assertThat(authority(host), equalTo(authority(host)));
        assertThat(authority(host).hashCode(), equalTo(authority(host).hashCode()));
    }

    @Test
    public void anAuthorityWithNoUserInfoOrPortIsNotEqualToAnotherWithTheADifferentHost() throws Exception {
        assertThat(authority(aHost()), not(equalTo(authority(aHost()))));
    }

    @Test
    public void anAuthorityWithNoUserInfoOrPortToStringIsCorrect() throws Exception {
        Host host = aHost();
        assertThat(authority(host).toString(), equalTo("Authority{host=" + host + "}"));
    }

    @Test
    public void makesAuthorityWithNoPort() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        assertThat(authority(userInfo, host).asString(), equalTo(userInfo.asString() + "@" + host.asString()));
    }

    @Test
    public void makesAuthorityWithNoUserInfo() throws Exception {
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(host, port).asString(), equalTo(host.asString() + ":" + port.asString()));
    }

    @Test
    public void makesAuthorityWithAllOptionsSpecified() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(userInfo, host, port).asString(), equalTo(userInfo.asString() + "@" + host.asString() + ":" + port.asString()));
    }

}
