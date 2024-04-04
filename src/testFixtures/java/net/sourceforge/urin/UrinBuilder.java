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

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.PathBuilder.anUnpollutedAbsolutePath;
import static net.sourceforge.urin.PathBuilder.anUnpollutedPath;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;

public final class UrinBuilder {

    private static final RandomSupplierSwitcher<Urin<String, Query<String>, Fragment<String>>> RANDOM_UNPOLLUTED_URIN_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            () -> aScheme().urin(),
            () -> aScheme().urin(anUnpollutedPath()),
            () -> aScheme().urin(anAuthority()),
            () -> aScheme().urin(anAuthority(), anUnpollutedAbsolutePath()),
            () -> aScheme().urin(aFragment()),
            () -> aScheme().urin(anUnpollutedPath(), aFragment()),
            () -> aScheme().urin(anAuthority(), aFragment()),
            () -> aScheme().urin(anAuthority(), anUnpollutedAbsolutePath(), aFragment()),
            () -> aScheme().urin(aQuery()),
            () -> aScheme().urin(anUnpollutedPath(), aQuery()),
            () -> aScheme().urin(anAuthority(), aQuery()),
            () -> aScheme().urin(anAuthority(), anUnpollutedAbsolutePath(), aQuery()),
            () -> aScheme().urin(aQuery(), aFragment()),
            () -> aScheme().urin(anUnpollutedPath(), aQuery(), aFragment()),
            () -> aScheme().urin(anAuthority(), aQuery(), aFragment()),
            () -> aScheme().urin(anAuthority(), anUnpollutedAbsolutePath(), aQuery(), aFragment())
    );

    private static final RandomSupplierSwitcher<Urin<String, Query<String>, Fragment<String>>> RANDOM_POLLUTED_URIN_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            () -> aScheme().urin(),
            () -> aScheme().urin(aPath()),
            () -> aScheme().urin(anAuthority()),
            () -> aScheme().urin(anAuthority(), anAbsolutePath()),
            () -> aScheme().urin(aFragment()),
            () -> aScheme().urin(aPath(), aFragment()),
            () -> aScheme().urin(anAuthority(), aFragment()),
            () -> aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()),
            () -> aScheme().urin(aQuery()),
            () -> aScheme().urin(aPath(), aQuery()),
            () -> aScheme().urin(anAuthority(), aQuery()),
            () -> aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()),
            () -> aScheme().urin(aQuery(), aFragment()),
            () -> aScheme().urin(aPath(), aQuery(), aFragment()),
            () -> aScheme().urin(anAuthority(), aQuery(), aFragment()),
            () -> aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment())
    );

    private UrinBuilder() {
    }

    public static Urin<String, Query<String>, Fragment<String>> aUrin() {
        return RANDOM_POLLUTED_URIN_SUPPLIER_SWITCHER.get();
    }

    public static Urin<String, Query<String>, Fragment<String>> anUnpollutedUrin() {
        return RANDOM_UNPOLLUTED_URIN_SUPPLIER_SWITCHER.get();
    }

}
