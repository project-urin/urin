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

import java.util.Random;

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static net.sourceforge.urin.SegmentsBuilder.anAbsoluteSegments;

public class HierarchicalPartBuilder {

    private static final Random RANDOM = new Random();

    public static HierarchicalPart aHierarchicalPart() {
        final HierarchicalPart hierarchicalPart;
        switch (RANDOM.nextInt(4)) {
            case 0:
                hierarchicalPart = HierarchicalPart.hierarchicalPart();
                break;
            case 1:
                hierarchicalPart = aHierarchicalPartWithAuthorityAndNoPath();
                break;
            case 2:
                hierarchicalPart = aHierarchicalPartWithNoAuthority();
                break;
            case 3:
                hierarchicalPart = aHierarchicalPartAbsoluteWithAuthority();
                break;
            default:
                throw new Defect("Attempted to switch on more cases than are defined");
        }
        return hierarchicalPart;
    }

    public static HierarchicalPart aHierarchicalPartWithAuthorityAndNoPath() {
        return hierarchicalPart(anAuthority());
    }

    public static HierarchicalPart aHierarchicalPartWithNoAuthority() {
        return hierarchicalPart(aSegments());
    }

    public static HierarchicalPart aHierarchicalPartAbsoluteWithAuthority() {
        return HierarchicalPart.hierarchicalPart(anAuthority(), anAbsoluteSegments());
    }

}
