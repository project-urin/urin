package net.sourceforge.urin;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SchemeTest {
    @Test
    public void lowerCaseSchemesAreUnmolested() throws Exception {
        assertThat(new Scheme("http").value(), equalTo("http"));
    }

    @Test
    public void schemesAreLowerCased() throws Exception {
        assertThat(new Scheme("HTTP").value(), equalTo("http"));
    }

    @Test
    public void rejectsInvalidSchemeNameFirstChar() throws Exception {
        try {
            new Scheme(".");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z, but was [.]"));
        }
    }

}
