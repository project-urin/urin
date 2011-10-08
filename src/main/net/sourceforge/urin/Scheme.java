package net.sourceforge.urin;

public final class Scheme {

    private final String value;

    public Scheme(final String name) {
        value = name.toLowerCase();
    }

    public String value() {
        return value;
    }
}
