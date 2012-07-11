/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.documentation;

import net.sourceforge.urin.Host;
import org.sourceforge.xazzle.xhtml.HtmlTag;

import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.documentation.UrinPage.aUrinPage;
import static net.sourceforge.urin.documentation.UrinPage.href;
import static net.sourceforge.urin.scheme.Https.https;
import static org.sourceforge.xazzle.xhtml.Tags.*;

final class SupportPage {

    private static final Host SOURCE_FORGE = registeredName("sourceforge.net");

    private SupportPage() {
    }

    @SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
    static HtmlTag supportPage() {
        return aUrinPage(
                h2Tag(xhtmlText("Support")),
                paragraphTag(
                        xhtmlText("The best way to get help on Urin is via the "),
                        anchorTag(xhtmlText("help forum")).withHref(href(https(SOURCE_FORGE, path("p", "urin", "discussion", "help")))),
                        xhtmlText(".")),
                paragraphTag(
                        xhtmlText("Alternatively, report a bug or request a feature by "),
                        anchorTag(xhtmlText("raising a ticket")).withHref(href(https(SOURCE_FORGE, path("p", "urin", "tickets")))),
                        xhtmlText(".")
                ),
                paragraphTag(
                        xhtmlText("Finally, there is an "),
                        anchorTag(xhtmlText("open discussion forum")).withHref(href(https(SOURCE_FORGE, path("p", "urin", "discussion", "general")))),
                        xhtmlText(" for anything else.")
                )
        );
    }
}
