package net.sourceforge.urin;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SchemeTest {
    @Test
    public void lowerCaseSchemesAreUnmolested() throws Exception {
        assertThat(new Scheme("a").value(), equalTo("a"));
    }

    @Test
    public void schemesAreLowerCased() throws Exception {
        assertThat(new Scheme("A").value(), equalTo("a"));
    }

    @Test
    public void acceptsTheFullRangeOfValidFirstCharacters() throws Exception {
        new Scheme("a");
        new Scheme("z");
        new Scheme("A");
        new Scheme("Z");
    }

    @Test
    public void rejectsNullScheme() throws Exception {
        try {
            new Scheme(null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expect to end up here
        }
    }

    @Test
    public void rejectsZeroLengthStringScheme() throws Exception {
        try {
            new Scheme("");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Scheme must contain at least one character"));
        }
    }

    @Test
    public void rejectsInvalidSchemeNameFirstChar() throws Exception {
        try {
            new Scheme("+");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [+]"));
        }
        try {
            new Scheme("-");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [-]"));
        }
        try {
            new Scheme(".");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [.]"));
        }
        try {
            new Scheme("4");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [4]"));
        }
        try {
            new Scheme("@");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [@]"));
        }
        try {
            new Scheme("[");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [[]"));
        }
        try {
            new Scheme("`");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [`]"));
        }
        try {
            new Scheme("{");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("First character must be a-z or A-Z in scheme [{]"));
        }
    }

    @Test
    public void acceptsFullRangeOfTailCharacters() throws Exception {
        new Scheme("aa");
        new Scheme("az");
        new Scheme("aA");
        new Scheme("aZ");
        new Scheme("a0");
        new Scheme("a9");
        new Scheme("a+");
        new Scheme("a-");
        new Scheme("a.");
    }

    @Test
    public void rejectsInvalidSchemeNameTailChar() throws Exception {
        try {
            new Scheme("a@");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a@]"));
        }
        try {
            new Scheme("a[");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a[]"));
        }
        try {
            new Scheme("a`");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a`]"));
        }
        try {
            new Scheme("a{");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a{]"));
        }
        try {
            new Scheme("a/");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a/]"));
        }
        try {
            new Scheme("a:");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Character 2 must be a-z, A-Z, 0-9, +, -, or . in scheme [a:]"));
        }
    }
}
