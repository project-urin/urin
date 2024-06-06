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

import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.urin.CharacterSetMembershipFunction.*;

final class PercentEncoder {
    static final PercentEncoder ENCODE_NOTHING = new PercentEncoder(ALL_CHARACTERS);
    static final PercentEncoder ENCODE_EVERYTHING = new PercentEncoder(NO_CHARACTERS);

    private static final Locale NO_LOCALISATION = null;
    private static final byte BINARY_1000_0000 = -128;
    private static final byte BINARY_1100_0000 = -64;
    private static final byte BINARY_1110_0000 = -32;
    private static final byte BINARY_1111_0000 = -16;

    private final CharacterSetMembershipFunction nonPercentEncodedCharacterSet;

    PercentEncoder(final CharacterSetMembershipFunction nonPercentEncodedCharacterSet) {
        this.nonPercentEncodedCharacterSet = nonPercentEncodedCharacterSet;
    }

    private static String percentEncode(final byte character) {
        return String.format(NO_LOCALISATION, "%%%02X", character);
    }

    private static byte getByte(final char[] source, final int startIndex) throws ParseException {
        if (source.length <= startIndex + 2 || '%' != source[startIndex]) {
            throw new ParseException("Cannot extract a percent encoded byte from [" + String.valueOf(source) + "] starting at index [" + startIndex + "]");
        } else {
            final String hexByte = new String(source, startIndex + 1, 2);
            if (!HEX_DIGIT.areMembers(hexByte)) {
                throw new ParseException("Cannot extract a percent encoded byte from [" + String.valueOf(source) + "] starting at index [" + startIndex + "]: [" + hexByte + "] is not a valid hex byte String");
            }
            try {
                return (byte) Integer.parseInt(hexByte, 16);
            } catch (NumberFormatException e) {
                throw new ParseException("Cannot extract a percent encoded byte from [" + String.valueOf(source) + "] starting at index [" + startIndex + "]", e);
            }
        }
    }

    private static int getByteCount(final byte firstByte) throws ParseException {
        final int byteCount;
        if ((firstByte & BINARY_1000_0000) == 0) {
            byteCount = 1;
        } else if ((firstByte & BINARY_1111_0000) == BINARY_1111_0000) {
            byteCount = 4;
        } else if ((firstByte & BINARY_1110_0000) == BINARY_1110_0000) {
            byteCount = 3;
        } else if ((firstByte & BINARY_1100_0000) == BINARY_1100_0000) {
            byteCount = 2;
        } else {
            throw new ParseException("First byte of a percent encoded character must begin 0, 11, 111, or 1111, but was " + firstByte);
        }
        return byteCount;
    }

    PercentEncoder additionallyEncoding(final char additionallyEncodedCharacter) {
        return new PercentEncoder(nonPercentEncodedCharacterSet.remove(additionallyEncodedCharacter));
    }

    String encode(final String notEncoded) {
        final StringBuilder result = new StringBuilder();
        for (final byte character : notEncoded.getBytes(UTF_8)) {
            if (nonPercentEncodedCharacterSet.isMember((char) character)) {
                result.append((char) character);
            } else {
                result.append(percentEncode(character));
            }
        }
        return result.toString();
    }

    String decode(final String encoded) throws ParseException {
        final StringBuilder result = new StringBuilder();
        final byte[] buffer = new byte[4];
        final char[] candidateChars = encoded.toCharArray();
        for (int i = 0; i < candidateChars.length; i++) {
            final char candidateChar = candidateChars[i];
            if ('%' == candidateChar) {
                buffer[0] = getByte(candidateChars, i);
                final int byteCount = getByteCount(buffer[0]);
                for (int j = 1; j < byteCount; j++) {
                    buffer[j] = getByte(candidateChars, i + (3 * j));
                }
                i = i + (3 * byteCount) - 1;
                result.append(new String(buffer, 0, byteCount, UTF_8));
            } else {
                if (nonPercentEncodedCharacterSet.isMember(candidateChar)) {
                    result.append(candidateChar);
                } else {
                    throw new ParseException("Invalid character [" + candidateChar + "] - must be " + nonPercentEncodedCharacterSet.describe());
                }
            }
        }
        return result.toString();
    }


}
