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

import static java.util.Locale.ENGLISH;

/**
 * A name component of a URI that refers to a scheme that is associated with a default port.
 */
public class SchemeWithDefaultPort<Q extends Query> extends Scheme<Q> {
    private final String name;
    private final Port defaultPort;

    /**
     * Constructor for subclasses of {@code Scheme} with default ports.
     *
     * @param name        the name of the scheme.
     * @param defaultPort the default port associated with the scheme.
     * @param queryParser the parser to use for parsing queries of this scheme.
     */
    protected SchemeWithDefaultPort(final String name, final Port defaultPort, final Parser<Q> queryParser) {
        super(queryParser);
        this.name = name;
        if (defaultPort == null) {
            throw new NullPointerException("Cannot instantiate Scheme with null default port");
        }
        this.defaultPort = defaultPort;
    }

    @Override
    SchemeWithDefaultPort<Q> withName(final String name) {
        return new SchemeWithDefaultPort<Q>(name, defaultPort, queryParser);
    }

    @Override
    String asString() {
        return name.toLowerCase(ENGLISH);
    }

    @Override
    Authority normalise(final Authority authority) {
        return authority.removePort(defaultPort);
    }

    @Override
    Scheme removeDefaultPort() {
        return scheme(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchemeWithDefaultPort that = (SchemeWithDefaultPort) o;

        return defaultPort.equals(that.defaultPort)
                && name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + defaultPort.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Scheme{" +
                "name='" + name + '\'' +
                ", defaultPort=" + defaultPort +
                '}';
    }
}
