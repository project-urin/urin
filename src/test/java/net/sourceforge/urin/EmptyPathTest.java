/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.PathBuilder.aRootlessPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmptyPathTest {
    @Test
    void anEmptyPathIsEqualToAnotherEmptyPath() {
        assertThat(new EmptyPath<>(), equalTo(new EmptyPath<>()));
        assertThat(new EmptyPath<>().hashCode(), equalTo(new EmptyPath<>().hashCode()));
    }

    @Test
    void aPathToStringIsCorrect() {
        assertThat(new EmptyPath<>().toString(), equalTo("EmptyPath"));
    }

    @Test
    void correctlyIdentifiesFirstPartNotSupplied() {
        assertThat(new EmptyPath<>().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    void resolvesEmptyPath() {
        assertThat(new EmptyPath<String>().resolveRelativeTo(new EmptyPath<>()), equalTo(new EmptyPath<>()));
    }

    @Test
    void resolvesAbsolutePath() {
        Path<String> basePath = anAbsolutePath();
        assertThat(new EmptyPath<String>().resolveRelativeTo(basePath), equalTo(basePath));
    }

    @Test
    void resolvesRootlessPath() {
        Path<String> basePath = aRootlessPath();
        assertThat(new EmptyPath<String>().resolveRelativeTo(basePath), equalTo(basePath));
    }

    @Test
    void emptyPathIsNotAbsolute() {
        assertThat(new EmptyPath<>().isAbsolute(), equalTo(false));
    }

    @Test
    void emptyPathIteratorIsEmpty() {
        assertThat(new EmptyPath<>(), Matchers.emptyIterable());
    }

    @Test
    void emptyPathSegmentsIsEmpty() {
        assertThat(new EmptyPath<String>().segments(), Matchers.empty());
    }

    @Test
    void emptyPathSegmentsDoesNotExposeMutability() {
        EmptyPath<String> emptyPath = new EmptyPath<>();
        assertThrows(UnsupportedOperationException.class, () -> {
            //noinspection ConstantConditions
            emptyPath.segments().add(aSegment());
            assertThat(emptyPath, Matchers.emptyIterable());
        }, "Null value should throw NullPointerException in factory");
    }
}
