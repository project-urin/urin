/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.documentation;

import net.sourceforge.urin.ParseException;
import net.sourceforge.urin.Segment;
import net.sourceforge.xazzle.xhtml.HtmlTag;

import java.util.List;

import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.documentation.UrinPage.*;
import static net.sourceforge.urin.scheme.http.Http.*;
import static net.sourceforge.xazzle.xhtml.Tags.*;

final class IndexPage {

    private IndexPage() {
    }

    public static HtmlTag indexPage(final String version) {
        return aUrinPage(
                h2Tag(xhtmlText("Introduction")),
                paragraphTag(xhtmlText("Urin is a URI generator and parser for Java.  It is written to make the dynamic generation of " +
                        "URIs easier than it is with Java's built-in URI and URL classes, and to provide support for the current URI standard, "),
                        RFC_3986, xhtmlText(".  It is open source, and free for you to use.")),
                paragraphTag(
                        xhtmlText("The latest version of Urin available for download is "),
                        anchorTag(xhtmlText(version)).withHref(href(standardJarUrin(version))),
                        xhtmlText(".  The "),
                        anchorTag(xhtmlText("javadoc")).withHref(href(HTTP.relativeReference(rootlessPath("javadoc")))),
                        xhtmlText(" is also available online.")
                ),
                h2Tag(xhtmlText("Example")),
                paragraphTag(xhtmlText("A brief example demonstrates the generation of an HTTP URI:")),
                codeBlock("import static net.sourceforge.urin.Host.*;\n" +
                        "import static net.sourceforge.urin.Path.*;\n" +
                        "import static net.sourceforge.urin.scheme.http.Http.*;\n" +
                        "\n" +
                        "...\n" +
                        "\n" +
                        "http(\n" +
                        "    registeredName(\"www.example.com\"),\n" +
                        "    path(\"music\", \"AC/DC\", \"Back in Black\")\n" +
                        ").asString();"),
                paragraphTag(xhtmlText("This produces the "), simpleNameOf(String.class), xhtmlText(" \""), codeSnippet(acDcUri()),
                        xhtmlText("\".  Note that the '"), codeSnippet("/"), xhtmlText("' character in \""), codeSnippet("AC/DC"),
                        xhtmlText("\" has been encoded as \""), codeSnippet("%2F"), xhtmlText("\", and that the space characters in \""),
                        codeSnippet("Back in Black"), xhtmlText("\" have been encoded as \""), codeSnippet("%20"), xhtmlText("\".")),
                paragraphTag(xhtmlText("In the above example, the registered name and path could be any Java "), simpleNameOf(String.class),
                        xhtmlText("s we choose; the library will encode them appropriately to the part of the URI where they appear.")),
                paragraphTag(xhtmlText("It's equally easy to go back to the non-encoded version:")),
                codeBlock("import static net.sourceforge.urin.scheme.http.Http.*;\n" +
                        "\n" +
                        "...\n" +
                        "\n" +
                        "parseHttpUrin(\n" +
                        "        \"http://www.example.com/music/AC%2FDC/Back%20in%20Black\"\n" +
                        ").path().segments();\n"),
                paragraphTag(xhtmlText("This produces a "), codeSnippet("List"), xhtmlText(" of three "), codeSnippet("Segment"),
                        xhtmlText("s, with the values \""), codeSnippet(acDcSegments().get(0).value()), xhtmlText("\", \""),
                        codeSnippet(acDcSegments().get(1).value()), xhtmlText("\", and \""),
                        codeSnippet(acDcSegments().get(2).value()), xhtmlText("\"."))
        );
    }

    private static String acDcUri() {
        return
                http(
                        registeredName("www.example.com"),
                        path("music", "AC/DC", "Back in Black")
                ).asString();
    }

    private static List<Segment<String>> acDcSegments() {
        try {
            return
                    parseHttpUrin(
                            "http://www.example.com/music/AC%2FDC/Back%20in%20Black"
                    ).path().segments();
        } catch (ParseException e) {
            throw new DocumentationGenerationException(e);
        }
    }

}
