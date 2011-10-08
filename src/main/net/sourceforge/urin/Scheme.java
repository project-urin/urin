package net.sourceforge.urin;

public final class Scheme {

    private final String value;

    public Scheme(final String name) {
        final char firstChar = name.charAt(0);
        if (!isAlpha(firstChar)) {
            throw new IllegalArgumentException("First character must be a-z or A-Z, but was [" + firstChar + "]");
        }
        value = name.toLowerCase();
    }

    private static boolean isAlpha(final char testChar) {
        return (testChar >= 'a' && testChar <= 'z')
                || (testChar >= 'A' && testChar <= 'Z');
    }

    public String value() {
        return value;
    }
}
