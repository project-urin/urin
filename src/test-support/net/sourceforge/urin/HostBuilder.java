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

import java.util.Random;

import static net.sourceforge.urin.CharacterSets.*;
import static net.sourceforge.urin.HexadectetBuilder.aHexadectet;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.OctetBuilder.anOctet;
import static org.apache.commons.lang3.RandomStringUtils.random;

public class HostBuilder {

    private static final Random RANDOM = new Random();

    public static Host aHost() {
        final Host host;
        switch (RANDOM.nextInt(4)) {
            case 0:
                host = aRegisteredName();
                break;
            case 1:
                host = anIpV4Address();
                break;
            case 2:
                host = anIpV6Address();
                break;
            case 3:
                host = anIpVFutureAddress();
                break;
            default:
                throw new Defect("Attempted to switch on more cases than are defined");
        }
        return host;
    }

    public static Host aRegisteredName() {
        return registeredName(random(5));
    }

    public static Host anIpV4Address() {
        return ipV4Address(anOctet(), anOctet(), anOctet(), anOctet());
    }

    public static Host anIpV6Address() {
        return ipV6Address(aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet(), aHexadectet());
    }

    public static Host anIpVFutureAddress() {
        return ipVFutureAddress(random(5, HEX_DIGIT), random(5, UNRESERVED + SUB_DELIMS + ":"));
    }
}
