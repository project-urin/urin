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

import java.nio.charset.Charset;
import java.util.Locale;

import static net.sourceforge.urin.CharacterSetMembershipFunction.ALL_CHARACTERS;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;

final class PercentEncoder {
    static final PercentEncoder ENCODE_NOTHING = new PercentEncoder(ALL_CHARACTERS);
    static final PercentEncoder ENCODE_EVERYTHING = new PercentEncoder(NO_CHARACTERS);

    private static final Locale NO_LOCALISATION = null;
    private final CharacterSetMembershipFunction nonPercentEncodedCharacterSet;

    PercentEncoder(final CharacterSetMembershipFunction nonPercentEncodedCharacterSet) {
        this.nonPercentEncodedCharacterSet = nonPercentEncodedCharacterSet;
    }

    PercentEncoder additionallyEncoding(final char additionallyEncodedCharacter) {
        return new PercentEncoder(nonPercentEncodedCharacterSet.remove(additionallyEncodedCharacter));
    }

    String encode(final String notEncoded) {
        StringBuilder result = new StringBuilder();
        for (byte character : notEncoded.getBytes(Charset.forName("UTF-8"))) {
            if (nonPercentEncodedCharacterSet.isMember((char) character)) {
                result.append((char) character);
            } else {
                result.append(percentEncode(character));
            }
        }
        return result.toString();
    }

    private static String percentEncode(final byte character) {
        return String.format(NO_LOCALISATION, "%%%02X", character);
    }

}
