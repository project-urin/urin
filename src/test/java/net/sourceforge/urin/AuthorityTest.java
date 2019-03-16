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

import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Authority.parse;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.PortBuilder.aPortDifferentTo;
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
    public void rejectsNullHostInFactory() throws Exception {
        assertThrowsException("Null host should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(null);
            }
        });
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
    public void rejectsNullInFactoryForAuthorityWithNoPort() throws Exception {
        assertThrowsException("Null userInfo should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(null, aHost());
            }
        });
        assertThrowsException("Null host should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(aUserInfo(), null);
            }
        });
    }

    @Test
    public void anAuthorityWithNoPortIsEqualToAnotherWithTheSameHostAndUserInfo() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        assertThat(authority(userInfo, host), equalTo(authority(userInfo, host)));
        assertThat(authority(userInfo, host).hashCode(), equalTo(authority(userInfo, host).hashCode()));
    }

    @Test
    public void anAuthorityWithNoPortIsNotEqualToAnotherWithTheADifferentHostAndUserInfo() throws Exception {
        assertThat(authority(aUserInfo(), aHost()), not(equalTo(authority(aUserInfo(), aHost()))));
    }

    @Test
    public void anAuthorityWithNoPortToStringIsCorrect() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        assertThat(authority(userInfo, host).toString(), equalTo("Authority{userInfo=" + userInfo + ", host=" + host + "}"));
    }

    @Test
    public void makesAuthorityWithNoUserInfo() throws Exception {
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(host, port).asString(), equalTo(host.asString() + ":" + port.asString()));
    }

    @Test
    public void rejectsNullInFactoryForAuthorityWithNoUserInfo() throws Exception {
        assertThrowsException("Null host should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(null, aPort());
            }
        });
        assertThrowsException("Null port should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(aHost(), null);
            }
        });
    }

    @Test
    public void anAuthorityWithNoUserInfoIsEqualToAnotherWithTheSameHostAndPort() throws Exception {
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(host, port), equalTo(authority(host, port)));
        assertThat(authority(host, port).hashCode(), equalTo(authority(host, port).hashCode()));
    }

    @Test
    public void anAuthorityWithNoUserInfoIsNotEqualToAnotherWithTheADifferentHostAndPort() throws Exception {
        assertThat(authority(aHost(), aPort()), not(equalTo(authority(aHost(), aPort()))));
    }

    @Test
    public void anAuthorityWithNoUserInfoToStringIsCorrect() throws Exception {
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(host, port).toString(), equalTo("Authority{host=" + host + ", port=" + port + "}"));
    }

    @Test
    public void makesAuthorityWithAllOptionsSpecified() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(userInfo, host, port).asString(), equalTo(userInfo.asString() + "@" + host.asString() + ":" + port.asString()));
    }

    @Test
    public void rejectsNullInFactoryForAuthorityWithAllOptions() throws Exception {
        assertThrowsException("Null userInfo should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(null, aHost(), aPort());
            }
        });
        assertThrowsException("Null host should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(aUserInfo(), null, aPort());
            }
        });
        assertThrowsException("Null port should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                authority(aUserInfo(), aHost(), null);
            }
        });
    }


    @Test
    public void anAuthorityWithAllOptionsSpecifiedIsEqualToAnotherWithTheSameFields() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(userInfo, host, port), equalTo(authority(userInfo, host, port)));
        assertThat(authority(userInfo, host, port).hashCode(), equalTo(authority(userInfo, host, port).hashCode()));
    }

    @Test
    public void anAuthorityWithAllOptionsSpecifiedIsNotEqualToAnotherWithTheADifferentHostUserInfoAndPort() throws Exception {
        assertThat(authority(aUserInfo(), aHost(), aPort()), not(equalTo(authority(aUserInfo(), aHost(), aPort()))));
    }

    @Test
    public void anAuthorityWithAllOptionsSpecifiedToStringIsCorrect() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        Port port = aPort();
        assertThat(authority(userInfo, host, port).toString(), equalTo("Authority{userInfo=" + userInfo + ", host=" + host + ", port=" + port + "}"));
    }

    @Test
    public void parsesAuthorityWithNoUserInfoOrPort() throws Exception {
        Host host = aHost();
        assertThat(parse(host.asString()), equalTo(authority(host)));
    }

    @Test
    public void parsesAuthorityWithNoPort() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        assertThat(parse(userInfo.asString() + "@" + host.asString()), equalTo(authority(userInfo, host)));
    }

    @Test
    public void parsesAuthorityWithNoUserInfo() throws Exception {
        Host host = aHost();
        Port port = aPort();
        assertThat(parse(host.asString() + ":" + port.asString()), equalTo(authority(host, port)));
    }

    @Test
    public void parsesAuthorityWithAllOptionsSpecified() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        Port port = aPort();
        assertThat(parse(userInfo.asString() + "@" + host.asString() + ":" + port.asString()), equalTo(authority(userInfo, host, port)));
    }

    @Test
    public void parsingAnInvalidAuthorityStringThrowsParseException() throws Exception {
        assertThrowsException("In invalid authority should throw a parse exception", ParseException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws ParseException {
                //noinspection NullableProblems
                Authority.parse("something invalid");
            }
        });
    }


    @Test
    public void parsingNullThrowsNullPointerException() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in parser", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws Exception {
                //noinspection NullableProblems
                Authority.parse(null);
            }
        });
    }


    @Test
    public void authorityWithNoUserInfoAndNoPortIsReturnedUnmolestedFromRemovingPort() throws Exception {
        Authority authority = authority(aHost());
        assertThat(authority.removePort(aPort()), equalTo(authority));
    }

    @Test
    public void authorityWithUserInfoAndNoPortIsReturnedUnmolestedFromRemovingPort() throws Exception {
        Authority authority = authority(aUserInfo(), aHost());
        assertThat(authority.removePort(aPort()), equalTo(authority));
    }

    @Test
    public void authorityWithPortIsReturnedUnmolestedFromRemovingDifferentPort() throws Exception {
        Port port = aPort();
        Authority authority = authority(aHost(), port);
        assertThat(authority.removePort(aPortDifferentTo(port)), equalTo(authority));
    }

    @Test
    public void authorityWithPortCorrectlyRemovesThatPort() throws Exception {
        Port port = aPort();
        Host host = aHost();
        Authority authority = authority(host, port);
        assertThat(authority.removePort(port), equalTo(authority(host)));
    }

    @Test
    public void authorityWithUserInfoAndPortIsReturnedUnmolestedFromRemovingDifferentPort() throws Exception {
        Port port = aPort();
        Authority authority = authority(aUserInfo(), aHost(), port);
        assertThat(authority.removePort(aPortDifferentTo(port)), equalTo(authority));
    }

    @Test
    public void authorityWithUserInfoAndPortCorrectlyRemovesThatPort() throws Exception {
        UserInfo userInfo = aUserInfo();
        Host host = aHost();
        Port port = aPort();
        Authority authority = authority(userInfo, host, port);
        assertThat(authority.removePort(port), equalTo(authority(userInfo, host)));
    }
}