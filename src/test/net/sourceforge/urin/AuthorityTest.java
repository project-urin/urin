package net.sourceforge.urin;

import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;

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

    private Port aPort() {
        return new Port();
    }

    private static UserInfo aUserInfo() {
        return new UserInfo();
    }

    private static Host aHost() {
        return new Host();
    }
}
