package net.sourceforge.urin;

import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.UserInfoBuilder.aUserInfo;

public class AuthorityTest {
    @Test
    public void makesAuthorityWithNoUserInfoOrPort() throws Exception {
        authority(aHost());
    }

    @Test
    public void makesAuthorityWithNoPort() throws Exception {
        authority(aUserInfo(), aHost());
    }

    @Test
    public void makesAuthorityWithNoUserInfo() throws Exception {
        authority(aHost(), aPort());
    }

    @Test
    public void makesAuthorityWithAllOptionsSpecified() throws Exception {
        authority(aUserInfo(), aHost(), aPort());
    }

}
