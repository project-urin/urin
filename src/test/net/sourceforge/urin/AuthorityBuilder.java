package net.sourceforge.urin;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HostBuilder.aHost;

public class AuthorityBuilder {
    static Authority anAuthority() {
        return authority(aHost());
    }
}
