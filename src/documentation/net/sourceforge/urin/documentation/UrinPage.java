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
import net.sourceforge.urin.Path;
import net.sourceforge.urin.Urin;
import org.sourceforge.xazzle.xhtml.*;

import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.scheme.Http.http;
import static net.sourceforge.urin.scheme.Http.https;
import static org.sourceforge.xazzle.xhtml.AlternateText.alternateText;
import static org.sourceforge.xazzle.xhtml.ClassName.className;
import static org.sourceforge.xazzle.xhtml.Href.href;
import static org.sourceforge.xazzle.xhtml.Id.id;
import static org.sourceforge.xazzle.xhtml.ImageSource.imageSource;
import static org.sourceforge.xazzle.xhtml.MetaContent.metaContent;
import static org.sourceforge.xazzle.xhtml.MetaName.metaName;
import static org.sourceforge.xazzle.xhtml.MimeType.mimeType;
import static org.sourceforge.xazzle.xhtml.Relationship.STYLESHEET;
import static org.sourceforge.xazzle.xhtml.Relationship.relationship;
import static org.sourceforge.xazzle.xhtml.Tags.*;
import static org.sourceforge.xazzle.xhtml.XhtmlDimension.pixels;

final class UrinPage {
    static final Host SOURCEFORGE = registeredName("sourceforge.net");
    static final Host W3_JIGSAW = registeredName("jigsaw.w3.org");
    static final Host W3_WWW = registeredName("www.w3.org");

    public static HtmlTag aUrinPage(final BodyElement... body) {
        final Href projectSiteHref = href(http(SOURCEFORGE, path("projects", "urin")).asString());
        return htmlTag(
                headTag(
                        titleTag("Urin - A Java library for making URIs"),
                        linkTag()
                                .withRelationships(STYLESHEET)
                                .withMimeType(mimeType("text/css"))
                                .withHref(href("urin.css")),
                        linkTag()
                                .withRelationships(relationship("icon"))
                                .withMimeType(mimeType("image/png"))
                                .withHref(href("favicon-32x32.png")),
                        metaTag(metaName("description"), metaContent("Urin is a replacement for Java's URI and URL classes.  It implements RFC 3986.  It is free to download and use in your project.")),
                        scriptTag(mimeType("text/javascript"), xhtmlText("  var _gaq = _gaq || [];\n" +
                                "  _gaq.push(['_setAccount', 'UA-16431822-5']);\n" +
                                "  _gaq.push(['_trackPageview']);\n" +
                                "\n" +
                                "  (function() {\n" +
                                "    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n" +
                                "    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n" +
                                "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n" +
                                "  })();"))
                ),
                bodyTag(
                        divTag(
                                divTag(
                                        h1Tag(xhtmlText("Urin"))
                                ).withId(id("header")),
                                divTag(
                                        unorderedListTag(
                                                listItemTag(
                                                        anchorTag(xhtmlText("Home"))
                                                                .withHref(href(relativeReference(Path.rootlessPath("index.html")).asString()))
                                                ),
                                                listItemTag(
                                                        anchorTag(xhtmlText("Downloads"))
                                                                .withHref(href(relativeReference(Path.rootlessPath("downloads.html")).asString()))
                                                ),
                                                listItemTag(
                                                        anchorTag(xhtmlText("Support"))
                                                                .withHref(href(relativeReference(Path.rootlessPath("support.html")).asString()))
                                                ),
                                                listItemTag(
                                                        anchorTag(xhtmlText("Project Site"))
                                                                .withHref(projectSiteHref)
                                                )
                                        )
                                ).withId(id("navigation")),
                                divTag(body).withId(id("content")),
                                divTag(
                                        unorderedListTag(
                                                listItemTag(
                                                        anchorTag(
                                                                imageTag(
                                                                        imageSource(http(registeredName("sflogo.sourceforge.net"), path("sflogo.php"), query("group_id=605761&type=13")).asString()),
                                                                        alternateText("Get urin at SourceForge.net. Fast, secure and Free Open Source software downloads")
                                                                )
                                                                        .withHeight(pixels("30"))
                                                                        .withWidth(pixels("120"))
                                                        ).withHref(projectSiteHref)
                                                ),
                                                listItemTag(
                                                        anchorTag(
                                                                imageTag(
                                                                        imageSource(http(W3_JIGSAW, path("css-validator", "images", "vcss")).asString()),
                                                                        alternateText("Valid CSS!")
                                                                )
                                                                        .withHeight(pixels("31"))
                                                                        .withWidth(pixels("88"))
                                                        ).withHref(href(http(W3_JIGSAW, path("css-validator", "check", "referer")).asString()))
                                                ),
                                                listItemTag(
                                                        anchorTag(
                                                                imageTag(
                                                                        imageSource(http(W3_WWW, path("Icons", "valid-xhtml10")).asString()),
                                                                        alternateText("Valid XHTML 1.0 Strict")
                                                                )
                                                                        .withHeight(pixels("31"))
                                                                        .withWidth(pixels("88"))
                                                        ).withHref(href(http(registeredName("validator.w3.org"), path("check"), query("uri=referer")).asString()))
                                                )
                                        )
                                ).withId(id("footer"))
                        ).withId(id("root"))
                )
        );
    }

    static InlineTag variableName(String name) {
        return codeTag(xhtmlText(name));
    }

    static InlineTag methodName(String name) {
        return codeTag(xhtmlText(name));
    }

    static InlineTag codeSnippet(String snippet) {
        return codeTag(xhtmlText(snippet));
    }

    static BlockLevelTag codeBlock(String someCode) {
        return divTag(
                xhtmlText(someCode)
        ).withClass(className("code"));
    }

    static InlineTag simpleNameOf(final Class clazz) {
        return codeTag(xhtmlText(clazz.getSimpleName()));
    }

    static Urin standardJarUrin(final String version) {
        // http://sourceforge.net/projects/urin/files/0.48/urin-0.48.jar/download
        return https(registeredName("sourceforge.net"), path("projects", "urin", "files", version, "urin-" + version + ".jar", "download"));
    }
}
