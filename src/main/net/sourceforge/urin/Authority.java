package net.sourceforge.urin;

public abstract class Authority {

    public static Authority authority(final Host host) {
        return new Authority() {
            @Override
            public String asString() {
                return null;
            }
        };
    }

    static Authority authority(final UserInfo userInfo, final Host host) {
        return new Authority() {
            @Override
            public String asString() {
                return null;
            }
        };
    }

    static Authority authority(final Host host, final Port port) {
        return new Authority() {
            @Override
            public String asString() {
                return null;
            }
        };
    }

    static Authority authority(final UserInfo userInfo, final Host host, final Port port) {
        return new Authority() {
            @Override
            public String asString() {
                return null;
            }
        };
    }

    private Authority() {
    }

    public abstract String asString();


}
