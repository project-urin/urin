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

public class CharacterSets {
    public static final String LOWER_CASE_ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_CASE_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA = LOWER_CASE_ALPHA + UPPER_CASE_ALPHA;
    public static final String DIGIT = "0123456789";
    public static final String UNRESERVED = ALPHA + DIGIT + "-._~";
    public static final String SUB_DELIMS = "!$&'()*+,;=";
    public static final String P_CHARS = UNRESERVED + SUB_DELIMS + ":" + "@";
    public static final String QUERY_AND_FRAGMENT_CHARACTERS = P_CHARS + "/" + "?";
}
