package net.sourceforge.urin;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SchemeTest {
    @Test
    public void schemesAreLowerCased() throws Exception {
        assertThat(new Scheme("HTTP").value(), equalTo("http"));
    }
}
