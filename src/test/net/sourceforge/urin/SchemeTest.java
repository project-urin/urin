package net.sourceforge.urin;

import org.junit.Test;

public class SchemeTest {
    @Test
    public void canCreateAScheme() throws Exception {
        new Scheme("http");
    }
}
