/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static org.apache.commons.lang3.RandomStringUtils.random;

public class MoreRandomStringUtils {
    static String randomDifferentTo(final String aString, final int size) {
        String random = random(size);
        return random.equals(aString) ? random + random(1) : random;  //To change body of created methods use File | Settings | File Templates.
    }
}
