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

import static net.sourceforge.urin.Host.ipV4Address;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.OctetBuilder.anOctet;
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;

public class HostBuilder {
    static Host aHost() {
        final Host host;
        switch (new Random().nextInt(2)) {
            case 0:
                host = aRegisteredName();
                break;
            case 1:
                host = anIpV4Address();
                break;
            default:
                throw new Defect("Attempted to switch on more cases than are defined");
        }
        return host;
    }

    public static Host aRegisteredName() {
        return registeredName(randomAscii(5));
    }

    public static Host anIpV4Address() {
        return ipV4Address(anOctet(), anOctet(), anOctet(), anOctet());
    }
}
