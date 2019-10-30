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

import net.sourceforge.urin.Fragment;
import net.sourceforge.urin.Host;
import net.sourceforge.urin.Urin;
import net.sourceforge.urin.UrinReference;
import net.sourceforge.urin.scheme.http.HttpQuery;
import net.sourceforge.urin.scheme.http.Https;
import net.sourceforge.xazzle.xhtml.AnchorTag;
import net.sourceforge.xazzle.xhtml.BlockElement;
import net.sourceforge.xazzle.xhtml.DoesNotContainFormTag;
import net.sourceforge.xazzle.xhtml.Href;
import net.sourceforge.xazzle.xhtml.HtmlTag;
import net.sourceforge.xazzle.xhtml.InlineTag;

import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.scheme.http.Http.HTTP;
import static net.sourceforge.urin.scheme.http.Http.http;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;
import static net.sourceforge.xazzle.xhtml.AlternateText.alternateText;
import static net.sourceforge.xazzle.xhtml.ClassName.className;
import static net.sourceforge.xazzle.xhtml.Id.id;
import static net.sourceforge.xazzle.xhtml.ImageSource.imageSource;
import static net.sourceforge.xazzle.xhtml.MetaContent.metaContent;
import static net.sourceforge.xazzle.xhtml.MetaName.metaName;
import static net.sourceforge.xazzle.xhtml.MimeType.mimeType;
import static net.sourceforge.xazzle.xhtml.Relationship.STYLESHEET;
import static net.sourceforge.xazzle.xhtml.Relationship.relationship;
import static net.sourceforge.xazzle.xhtml.Tags.*;
import static net.sourceforge.xazzle.xhtml.XhtmlDimension.pixels;

final class UrinPage {
    static final Host SOURCEFORGE = registeredName("sourceforge.net");
    static final Host W3_JIGSAW = registeredName("jigsaw.w3.org");
    static final Host W3_WWW = registeredName("www.w3.org");
    static final AnchorTag RFC_3986 = anchorTag(xhtmlText("RFC 3986")).withHref(href(http(registeredName("tools.ietf.org"), path("html", "rfc3986"))));

    public static HtmlTag aUrinPage(final BlockElement<DoesNotContainFormTag>... body) {
        final Href projectSiteHref = href(http(SOURCEFORGE, path("projects", "urin")));
        return htmlTag(
                headTag(
                        titleTag("Urin - A Java library for making URIs"),
                        linkTag()
                                .withRelationships(STYLESHEET)
                                .withMimeType(mimeType("text/css"))
                                .withHref(href(HTTP.relativeReference(rootlessPath("urin.css")))),
                        linkTag()
                                .withRelationships(relationship("icon"))
                                .withMimeType(mimeType("image/png"))
                                .withHref(href(HTTP.relativeReference(rootlessPath("favicon-32x32.png")))),
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
                                                                .withHref(href(HTTP.relativeReference(rootlessPath("index.html"))))
                                                ),
                                                listItemTag(
                                                        anchorTag(xhtmlText("Downloads"))
                                                                .withHref(href(HTTP.relativeReference(rootlessPath("downloads.html"))))
                                                ),
                                                listItemTag(
                                                        anchorTag(xhtmlText("Documentation"))
                                                                .withHref(href(HTTP.relativeReference(rootlessPath("documentation.html"))))
                                                ),
                                                listItemTag(
                                                        anchorTag(xhtmlText("Support"))
                                                                .withHref(href(HTTP.relativeReference(rootlessPath("support.html"))))
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
                                                                        imageSource(http(registeredName("sflogo.sourceforge.net"), path("sflogo.php"), queryParameters(queryParameter("group_id", "605761"), queryParameter("type", "13"))).asString()),
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
                                                        ).withHref(href(http(W3_JIGSAW, path("css-validator", "check", "referer"))))
                                                ),
                                                listItemTag(
                                                        anchorTag(
                                                                imageTag(
                                                                        imageSource(http(W3_WWW, path("Icons", "valid-xhtml10")).asString()),
                                                                        alternateText("Valid XHTML 1.0 Strict")
                                                                )
                                                                        .withHeight(pixels("31"))
                                                                        .withWidth(pixels("88"))
                                                        ).withHref(href(http(registeredName("validator.w3.org"), path("check"), queryParameters(queryParameter("uri", "referer")))))
                                                )
                                        )
                                ).withId(id("footer"))
                        ).withId(id("root"))
                )
        );
    }

    static InlineTag codeSnippet(String snippet) {
        return codeTag(xhtmlText(snippet));
    }

    static BlockElement<DoesNotContainFormTag> codeBlock(String someCode) {
        return divTag(
                xhtmlText(someCode)
        ).withClass(className("code"));
    }

    static InlineTag simpleNameOf(final Class clazz) {
        return codeTag(xhtmlText(clazz.getSimpleName()));
    }

    static InlineTag canonicalNameOf(final Class clazz) {
        return codeTag(xhtmlText(clazz.getCanonicalName()));
    }

    static Urin<String, HttpQuery, Fragment<String>> standardJarUrin(final String version) {
        return Https.https(registeredName("sourceforge.net"), path("projects", "urin", "files", version, "urin-" + version + ".jar", "download"));
    }

    static Href href(final UrinReference urinReference) {
        return Href.href(urinReference.asString());
    }
}
