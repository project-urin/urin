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

import org.junit.Test;

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.HierarchicalPart.parse;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class HierarchicalPartTest {
    @Test
    public void aHierarchicalPartWithEmptyPathAsStringIsCorrect() throws Exception {
        assertThat(hierarchicalPart().asString(), equalTo(""));
    }

    @Test
    public void aHierarchicalPartWithEmptyPathIsEqualToAnotherHierarchicalPartWithEmptyPath() throws Exception {
        assertThat(hierarchicalPart(), equalTo(hierarchicalPart()));
        assertThat(hierarchicalPart().hashCode(), equalTo(hierarchicalPart().hashCode()));
    }

    @Test
    public void aHierarchicalPartWithEmptyPathToStringIsCorrect() throws Exception {
        assertThat(hierarchicalPart().toString(), equalTo("HierarchicalPart{path=EmptyPath}"));
    }

    @Test
    public void aSimpleAbsolutePathAsStringReturnsThePath() throws Exception {
        Path path = aPath();
        assertThat(hierarchicalPart(path).asString(), equalTo(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)));
    }

    @Test
    public void aSimpleAbsolutePathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aNonDotSegment();
        assertThat(hierarchicalPart(Path.path(firstSegment, secondSegment)).asString(), equalTo("/./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aHierarchicalPartWithOnlyPathResolvesPathToAHierarchicalPartWithOnlyPath() throws Exception {
        Path basePath = aPath();
        Path relativeReferencePath = aPath();
        assertThat(hierarchicalPart(basePath).resolve(relativeReferencePath), equalTo(hierarchicalPart(relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    public void aHierarchicalPartWithOnlyPathResolvesAuthorityAndPathToAHierarchicalPartWithSuppliedAuthorityAndMergedPath() throws Exception {
        Path basePath = aPath();
        Path relativeReferencePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(hierarchicalPart(basePath).resolve(relativeReferenceAuthority, relativeReferencePath), equalTo(hierarchicalPart(relativeReferenceAuthority, (AbsolutePath) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    public void rejectsNullInFactoryForASimplePath() throws Exception {
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path path = null;
                hierarchicalPart(path);
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() throws Exception {
        Path path = aPath();
        assertThat(hierarchicalPart(path), equalTo(hierarchicalPart(path)));
        assertThat(hierarchicalPart(path).hashCode(), equalTo(hierarchicalPart(path).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(hierarchicalPart(aPath()), not(equalTo(hierarchicalPart(aPath()))));
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Path path = aPath();
        assertThat(hierarchicalPart(path).toString(), equalTo("HierarchicalPart{path=" + path + "}"));
    }

    @Test
    public void aSimpleRootlessPathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aNonDotSegment();
        Segment secondSegment = aNonDotSegment();
        assertThat(hierarchicalPart(rootlessPath(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aNonDotSegment();
        assertThat(hierarchicalPart(rootlessPath(firstSegment, secondSegment)).asString(), equalTo("./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPart(rootlessPath(firstSegment, secondSegment)), equalTo(hierarchicalPart(rootlessPath(firstSegment, secondSegment))));
        assertThat(hierarchicalPart(rootlessPath(firstSegment, secondSegment)).hashCode(), equalTo(hierarchicalPart(rootlessPath(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(hierarchicalPart(rootlessPath(aSegment(), aSegment())), not(equalTo(hierarchicalPart(rootlessPath(aSegment(), aSegment())))));
    }

    @Test
    public void aSimpleRootlessPathToStringIsCorrect() throws Exception {
        Segment firstSegment = aNonDotSegment();
        Segment secondSegment = aNonDotSegment();
        assertThat(hierarchicalPart(rootlessPath(firstSegment, secondSegment)).toString(), equalTo("HierarchicalPart{path=[" + firstSegment + ", " + secondSegment + "]}"));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    public void rejectsNullInFactoryForHierarchicalPartWithAuthorityAndEmptyPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                hierarchicalPart(authority);
            }
        });
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndEmptyPathIsEqualToAnotherWithTheSameAuthority() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority), equalTo(hierarchicalPart(authority)));
        assertThat(hierarchicalPart(authority).hashCode(), equalTo(hierarchicalPart(authority).hashCode()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndEmptyPathIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        assertThat(hierarchicalPart(anAuthority()), not(equalTo(hierarchicalPart(anAuthority()))));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndEmptyPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority).toString(), equalTo("HierarchicalPart{authority=" + authority + ", path=EmptyPath}"));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aNonDotSegment();
        Segment secondSegment = aNonDotSegment();
        assertThat(hierarchicalPart(authority, Path.path(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndNonEmptyPathHasImmutableVarargs() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aNonDotSegment();
        Segment secondSegment = aNonDotSegment();
        Segment[] segments = {firstSegment, secondSegment};
        HierarchicalPart hierarchicalPart = hierarchicalPart(authority, Path.path(segments));
        segments[0] = aNonDotSegment();
        assertThat(hierarchicalPart.asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = Path.path();
        assertThat(hierarchicalPart(authority, absolutePath), equalTo(hierarchicalPart(authority, absolutePath)));
        assertThat(hierarchicalPart(authority, absolutePath).hashCode(), equalTo(hierarchicalPart(authority, absolutePath).hashCode()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(hierarchicalPart(anAuthority(), Path.path()), not(equalTo(hierarchicalPart(anAuthority(), Path.path()))));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = Path.path();
        assertThat(hierarchicalPart(authority, absolutePath).toString(), equalTo("HierarchicalPart{authority=" + authority + ", path=" + absolutePath + "}"));
    }

    @Test
    public void aHierarchicalPartWithAuthorityResolvesPathToAHierarchicalPartWithTheSameAuthority() throws Exception {
        Authority baseAuthority = anAuthority();
        AbsolutePath basePath = anAbsolutePath();
        Path relativeReferencePath = aPath();
        assertThat(hierarchicalPart(baseAuthority, basePath).resolve(relativeReferencePath), equalTo(hierarchicalPart(baseAuthority, (AbsolutePath) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    public void aHierarchicalPartWithAuthorityResolvesAuthorityAndPathToAHierarchicalPartWithSuppliedAuthorityAndPath() throws Exception {
        Authority baseAuthority = anAuthority();
        AbsolutePath basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        Path relativeReferencePath = anAbsolutePath();
        assertThat(hierarchicalPart(baseAuthority, basePath).resolve(relativeReferenceAuthority, relativeReferencePath), equalTo(hierarchicalPart(relativeReferenceAuthority, (AbsolutePath) relativeReferencePath)));
    }

    @Test
    public void rejectsNullInFactoryForHierarchicalPartWithAuthorityAndPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                hierarchicalPart(authority, Path.path());
            }
        });
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                hierarchicalPart(anAuthority(), null);
            }
        });
    }

    @Test
    public void parsesAHierarchicalPartWithEmptyPath() throws Exception {
        assertThat(parse(""), equalTo(hierarchicalPart()));
    }

    @Test
    public void parsesASimpleAbsolutePath() throws Exception {
        Path path = aPath();
        assertThat(parse(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)), equalTo(hierarchicalPart(path)));
    }

    @Test
    public void parsesASimpleRootlessPath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(parse(firstSegment.asString() + "/" + secondSegment.asString()), equalTo(hierarchicalPart(rootlessPath(firstSegment, secondSegment))));
    }

    @Test
    public void parsesHierarchicalPartWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(parse("//" + authority.asString()), equalTo(hierarchicalPart(authority)));
    }

    @Test
    public void parsesHierarchicalPartWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(parse("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(hierarchicalPart(authority, Path.path(firstSegment, secondSegment))));
    }

}
