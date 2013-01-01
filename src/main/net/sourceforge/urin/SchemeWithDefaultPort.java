/*
 * Copyright 2013 Mark Slater
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
 *
 * @param <QUERY> The type of {@code Query} used by this scheme.
 */
public class SchemeWithDefaultPort<SEGMENT, QUERY extends Query, FRAGMENT extends Fragment> extends Scheme<SEGMENT, QUERY, FRAGMENT> {
    private final String name;
    private final Port defaultPort;

    /**
     * Constructor for subclasses of {@code Scheme} with default ports.
     *
     * @param name                  the name of the scheme.
     * @param defaultPort           the default port associated with the scheme.
     * @param segmentMakingDecoder  the parser to use for parsing segments of this scheme.
     * @param queryMakingDecoder    the parser to use for parsing queries of this scheme.
     * @param fragmentMakingDecoder the parser to use for parsing fragments of this scheme.
     */
    protected SchemeWithDefaultPort(final String name, final Port defaultPort, final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder, final MakingDecoder<QUERY, ?, String> queryMakingDecoder, MakingDecoder<FRAGMENT, ?, String> fragmentMakingDecoder) {
        super(segmentMakingDecoder, queryMakingDecoder, fragmentMakingDecoder);
        this.name = name;
        if (defaultPort == null) {
            throw new NullPointerException("Cannot instantiate Scheme with null default port");
        }
        this.defaultPort = defaultPort;
    }

    private SchemeWithDefaultPort(final String name, final Port defaultPort, final Scheme<SEGMENT, QUERY, FRAGMENT> prototype) {
        super(prototype);
        this.name = name;
        if (defaultPort == null) {
            throw new NullPointerException("Cannot instantiate Scheme with null default port");
        }
        this.defaultPort = defaultPort;
    }

    @Override
    SchemeWithDefaultPort<SEGMENT, QUERY, FRAGMENT> withName(final String name) {
        return new SchemeWithDefaultPort<>(name, defaultPort, this);
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
    Scheme<SEGMENT, QUERY, FRAGMENT> removeDefaultPort() {
        return new GenericScheme<>(name, this);
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
