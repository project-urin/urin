/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.hamcrest.Matchers;
import org.junit.Test;

import static net.sourceforge.urin.PathBuilder.aRootlessPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class EmptyPathTest {
    @Test
    public void anEmptyPathIsEqualToAnotherEmptyPath() throws Exception {
        assertThat(new EmptyPath(), equalTo(new EmptyPath()));
        assertThat(new EmptyPath().hashCode(), equalTo(new EmptyPath().hashCode()));
    }

    @Test
    public void aPathToStringIsCorrect() throws Exception {
        assertThat(new EmptyPath().toString(), equalTo("EmptyPath"));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(new EmptyPath().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void resolvesEmptyPath() throws Exception {
        assertThat(new EmptyPath().resolveRelativeTo(new EmptyPath()), equalTo((Path) new EmptyPath()));
    }

    @Test
    public void resolvesAbsolutePath() throws Exception {
        Path basePath = anAbsolutePath();
        assertThat(new EmptyPath().resolveRelativeTo(basePath), equalTo(basePath));
    }

    @Test
    public void resolvesRootlessPath() throws Exception {
        Path basePath = aRootlessPath();
        assertThat(new EmptyPath().resolveRelativeTo(basePath), equalTo(basePath));
    }

    @Test
    public void emptyPathIsNotAbsolute() throws Exception {
        assertThat(new EmptyPath().isAbsolute(), equalTo(false));
    }

    @Test
    public void emptyPathIteratorIsEmpty() throws Exception {
        assertThat(new EmptyPath(), Matchers.<Segment>emptyIterable());
    }
}
