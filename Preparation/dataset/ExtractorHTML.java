/* Copyright (C) 2003 Internet Archive.
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * SimpleHTMLExtractor.java
 * Created on Jun 5, 2003
 *
 * $Header$
 */
package org.archive.crawler.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.RobotsHonoringPolicy;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;
import org.archive.io.ReplayCharSequence;
import org.archive.net.UURI;
import org.archive.net.UURIFactory;
import org.archive.util.DevUtils;
import org.archive.util.HttpRecorder;
import org.archive.util.TextUtils;
import org.archive.util.UriUtils;

/**
 * Basic link-extraction, from an HTML content-body,
 * using regular expressions.
 *
 * @author gojomo
 *
 */
public class ExtractorHTML extends Extractor
implements CoreAttributeConstants {

    private static final long serialVersionUID = 5855731422080471017L;

    private static Logger logger =
        Logger.getLogger(ExtractorHTML.class.getName());

    /**
     * Compiled relevant tag extractor.
     *
     * <p>
     * This pattern extracts either:
     * <li> (1) whole &lt;script&gt;...&lt;/script&gt; or
     * <li> (2) &lt;style&gt;...&lt;/style&gt; or
     * <li> (3) &lt;meta ...&gt; or
     * <li> (4) any other open-tag with at least one attribute
     * (eg matches "&lt;a href='boo'&gt;" but not "&lt;/a&gt;" or "&lt;br&gt;")
     * <p>
     * groups:
     * <li> 1: SCRIPT SRC=foo&gt;boo&lt;/SCRIPT
     * <li> 2: just script open tag
     * <li> 3: STYLE TYPE=moo&gt;zoo&lt;/STYLE
     * <li> 4: just style open tag
     * <li> 5: entire other tag, without '<' '>'
     * <li> 6: element
     * <li> 7: META
     * <li> 8: !-- comment --
     */
// version w/ less unnecessary backtracking
      private static final int MAX_ELEMENT_LENGTH =
          Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() +
              ".maxElementNameLength", "1024"));
      
      static final String RELEVANT_TAG_EXTRACTOR =
          "(?is)<(?:((script[^>]*+)>.*?</script)" + // 1, 2
          "|((style[^>]*+)>.*?</style)" + // 3, 4
          "|(((meta)|(?:\\w{1,"+MAX_ELEMENT_LENGTH+"}))\\s+[^>]*+)" + // 5, 6, 7
          "|(!--.*?--))>"; // 8 

//    version w/ problems with unclosed script tags 
//    static final String RELEVANT_TAG_EXTRACTOR =
//    "(?is)<(?:((script.*?)>.*?</script)|((style.*?)>.*?</style)|(((meta)|(?:\\w+))\\s+.*?)|(!--.*?--))>";


      
//    // this pattern extracts 'href' or 'src' attributes from
//    // any open-tag innards matched by the above
//    static Pattern RELEVANT_ATTRIBUTE_EXTRACTOR = Pattern.compile(
//     "(?is)(\\w+)(?:\\s+|(?:\\s.*?\\s))(?:(href)|(src))\\s*=(?:(?:\\s*\"(.+?)\")|(?:\\s*'(.+?)')|(\\S+))");
//
//    // this pattern extracts 'robots' attributes
//    static Pattern ROBOTS_ATTRIBUTE_EXTRACTOR = Pattern.compile(
//     "(?is)(\\w+)\\s+.*?(?:(robots))\\s*=(?:(?:\\s*\"(.+)\")|(?:\\s*'(.+)')|(\\S+))");

      private static final int MAX_ATTR_NAME_LENGTH =
          Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() +
              ".maxAttributeNameLength", "1024")); // 1K; 
      
      static final int MAX_ATTR_VAL_LENGTH = 
          Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() +
              ".maxAttributeValueLength", "16384")); // 16K; 
      
    // TODO: perhaps cut to near MAX_URI_LENGTH
    
    // this pattern extracts attributes from any open-tag innards
    // matched by the above. attributes known to be URIs of various
    // sorts are matched specially
    static final String EACH_ATTRIBUTE_EXTRACTOR =
      "(?is)\\b((href)|(action)|(on\\w*)" // 1, 2, 3, 4 
     +"|((?:src)|(?:lowsrc)|(?:background)|(?:cite)|(?:longdesc)" // ...
     +"|(?:usemap)|(?:profile)|(?:datasrc))" // 5
     +"|(codebase)|((?:classid)|(?:data))|(archive)|(code)" // 6, 7, 8, 9
     +"|(value)|(style)|(method)" // 10, 11, 12
     +"|([-\\w]{1,"+MAX_ATTR_NAME_LENGTH+"}))" // 13
     +"\\s*=\\s*"
     +"(?:(?:\"(.{0,"+MAX_ATTR_VAL_LENGTH+"}?)(?:\"|$))" // 14
     +"|(?:'(.{0,"+MAX_ATTR_VAL_LENGTH+"}?)(?:'|$))" // 15
     +"|(\\S{1,"+MAX_ATTR_VAL_LENGTH+"}))"; // 16
    // groups:
    // 1: attribute name
    // 2: HREF - single URI relative to doc base, or occasionally javascript:
    // 3: ACTION - single URI relative to doc base, or occasionally javascript:
    // 4: ON[WHATEVER] - script handler
    // 5: SRC,LOWSRC,BACKGROUND,CITE,LONGDESC,USEMAP,PROFILE, or DATASRC
    //    single URI relative to doc base
    // 6: CODEBASE - a single URI relative to doc base, affecting other
    //    attributes
    // 7: CLASSID, DATA - a single URI relative to CODEBASE (if supplied)
    // 8: ARCHIVE - one or more space-delimited URIs relative to CODEBASE
    //    (if supplied)
    // 9: CODE - a single URI relative to the CODEBASE (is specified).
    // 10: VALUE - often includes a uri path on forms
    // 11: STYLE - inline attribute style info
    // 12: METHOD - form GET/POST
    // 13: any other attribute
    // 14: double-quote delimited attr value
    // 15: single-quote delimited attr value
    // 16: space-delimited attr value

    static final String WHITESPACE = "\\s";
    static final String CLASSEXT =".class";
    static final String APPLET = "applet";
    static final String BASE = "base";
    static final String LINK = "link";
    static final String FRAME = "frame";
    static final String IFRAME = "iframe";

    public static final String ATTR_TREAT_FRAMES_AS_EMBED_LINKS =
        "treat-frames-as-embed-links";
    
    public static final String ATTR_IGNORE_FORM_ACTION_URLS =
        "ignore-form-action-urls";

    public static final String ATTR_EXTRACT_ONLY_FORM_GETS =
        "extract-only-form-gets";

    /** whether to try finding links in Javscript; default true */
    public static final String ATTR_EXTRACT_JAVASCRIPT =
        "extract-javascript";

    public static final String EXTRACT_VALUE_ATTRIBUTES =
        "extract-value-attributes";
    
    public static final String ATTR_IGNORE_UNEXPECTED_HTML = 
        "ignore-unexpected-html";

    
    protected long numberOfCURIsHandled = 0;
    protected long numberOfLinksExtracted = 0;

    public ExtractorHTML(String name) {
        this(name, "HTML extractor. Extracts links from HTML documents");
    }
    
    public ExtractorHTML(String name, String description) {
        super(name, description);
        Type t = addElementToDefinition(
            new SimpleType(ATTR_EXTRACT_JAVASCRIPT,
            "If true, in-page Javascript is scanned for strings that " +
            "appear likely to be URIs. This typically finds both valid " +
            "and invalid URIs, and attempts to fetch the invalid URIs " +
            "sometimes generates webmaster concerns over odd crawler " +
            "behavior. Default is true.",
            Boolean.TRUE));
        t.setExpertSetting(true);
        t = addElementToDefinition(
            new SimpleType(ATTR_TREAT_FRAMES_AS_EMBED_LINKS,
            "If true, FRAME/IFRAME SRC-links are treated as embedded " +
            "resources (like IMG, 'E' hop-type), otherwise they are " +
            "treated as navigational links. Default is true.", Boolean.TRUE));
        t.setExpertSetting(true);
        t = addElementToDefinition(
            new SimpleType(ATTR_IGNORE_FORM_ACTION_URLS,
            "If true, URIs appearing as the ACTION attribute in " +
            "HTML FORMs are ignored. Default is false.", Boolean.FALSE));
        t.setExpertSetting(true);
        t = addElementToDefinition(
                new SimpleType(ATTR_EXTRACT_ONLY_FORM_GETS,
                "If true, only HTML FORM ACTIONs associated with the GET "+ 
                "method are extracted. (Form ACTIONs with method POST "+
                "will be ignored. Default is true", Boolean.TRUE));
        t.setExpertSetting(true);
        t = addElementToDefinition(
            new SimpleType(EXTRACT_VALUE_ATTRIBUTES,
            "If true, strings that look like URIs found in element VALUE " +
            "attributes (which are sometimes used as URIs by in-page " +
            "Javascript or server-side redirects) will be extracted. " +
            "This typically finds both valid and invalid URIs, and " +
            "attempts to fetch the invalid URIs sometimes generate " +
            "webmaster concerns over odd crawler behavior. Default " +
            "is true.",
            Boolean.TRUE));
        t.setExpertSetting(true);
        t = addElementToDefinition(
            new SimpleType(ATTR_IGNORE_UNEXPECTED_HTML,
            "If true, URIs which end in typical non-HTML extensions " +
            "(such as .gif) will not be scanned as if it were HTML. " +
            "Default is true.", Boolean.TRUE));
        t.setExpertSetting(true);
    }

    protected void processGeneralTag(CrawlURI curi, CharSequence element,
            CharSequence cs) {

        Matcher attr = TextUtils.getMatcher(EACH_ATTRIBUTE_EXTRACTOR, cs);

        // Just in case it's an OBJECT or APPLET tag
        String codebase = null;
        ArrayList<String> resources = null;
        
        // Just in case it's a FORM
        CharSequence action = null;
        CharSequence actionContext = null;
        CharSequence method = null; 
        
        // Just in case it's a VALUE whose interpretation depends on accompanying NAME
        CharSequence valueVal = null; 
        CharSequence valueContext = null;
        CharSequence nameVal = null; 
        
        final boolean framesAsEmbeds = ((Boolean)getUncheckedAttribute(curi,
            ATTR_TREAT_FRAMES_AS_EMBED_LINKS)).booleanValue();

        final boolean ignoreFormActions = ((Boolean)getUncheckedAttribute(curi,
                ATTR_IGNORE_FORM_ACTION_URLS)).booleanValue();
        
        final boolean extractValueAttributes = ((Boolean)getUncheckedAttribute
                (curi, EXTRACT_VALUE_ATTRIBUTES)).booleanValue();
        
        final String elementStr = element.toString();
        while (attr.find()) {
            int valueGroup =
                (attr.start(14) > -1) ? 14 : (attr.start(15) > -1) ? 15 : 16;
            int start = attr.start(valueGroup);
            int end = attr.end(valueGroup);
            assert start >= 0: "Start is: " + start + ", " + curi;
            assert end >= 0: "End is :" + end + ", " + curi;
            CharSequence value = cs.subSequence(start, end);
            CharSequence attrName = cs.subSequence(attr.start(1),attr.end(1));
            value = TextUtils.unescapeHtml(value);
            if (attr.start(2) > -1) {
                // HREF
                CharSequence context =
                    Link.elementContext(element, attr.group(2));
                if(elementStr.equalsIgnoreCase(LINK)) {
                    // <LINK> elements treated as embeds (css, ico, etc)
                    processEmbed(curi, value, context);
                } else {
                    // other HREFs treated as links
                    processLink(curi, value, context);
                }
                if (elementStr.equalsIgnoreCase(BASE)) {
                    try {
                        curi.setBaseURI(value.toString());
                    } catch (URIException e) {
                        if (getController() != null) {
                            // Controller can be null: e.g. when running
                            // ExtractorTool.
                            getController().logUriError(e, curi.getUURI(),
                                value.toString());
                        } else {
                            logger.info("Failed set base uri: " +
                                curi + ", " + value.toString() + ": " +
                                e.getMessage());
                        }
                    }
                }
            } else if (attr.start(3) > -1) {
                // ACTION
                if (!ignoreFormActions) {
                    action = value; 
                    actionContext = Link.elementContext(element,
                        attr.group(3));
                    // handling finished only at end (after METHOD also collected)
                }
            } else if (attr.start(4) > -1) {
                // ON____
                processScriptCode(curi, value); // TODO: context?
            } else if (attr.start(5) > -1) {
                // SRC etc.
                CharSequence context = Link.elementContext(element,
                    attr.group(5));
                
                // true, if we expect another HTML page instead of an image etc.
                // TODO: add explicit 'F'rame hop type? (it's not really L, and
                // different enough from other 'E's)
                final char hopType;
                
                if(!framesAsEmbeds
                    && (elementStr.equalsIgnoreCase(FRAME) || elementStr
                        .equalsIgnoreCase(IFRAME))) {
                    hopType = Link.NAVLINK_HOP;
                } else {
                    hopType = Link.EMBED_HOP;
                }
                processEmbed(curi, value, context, hopType);
            } else if (attr.start(6) > -1) {
                // CODEBASE
                codebase = (value instanceof String)?
                    (String)value: value.toString();
                CharSequence context = Link.elementContext(element,
                    attr.group(6));
                processEmbed(curi, codebase, context);
            } else if (attr.start(7) > -1) {
                // CLASSID, DATA
                if (resources == null) {
                    resources = new ArrayList<String>();
                }
                resources.add(value.toString());
            } else if (attr.start(8) > -1) {
                // ARCHIVE
                if (resources==null) {
                    resources = new ArrayList<String>();
                }
                String[] multi = TextUtils.split(WHITESPACE, value);
                for(int i = 0; i < multi.length; i++ ) {
                    resources.add(multi[i]);
                }
            } else if (attr.start(9) > -1) {
                // CODE
                if (resources==null) {
                    resources = new ArrayList<String>();
                }
                // If element is applet and code value does not end with
                // '.class' then append '.class' to the code value.
                if (elementStr.equalsIgnoreCase(APPLET) &&
                        !value.toString().toLowerCase().endsWith(CLASSEXT)) {
                    resources.add(value.toString() + CLASSEXT);
                } else {
                    resources.add(value.toString());
                }
            } else if (attr.start(10) > -1) {
                // VALUE, with possibility of URI
                // store value, context for handling at end
                valueVal = value; 
                valueContext = Link.elementContext(element,attr.group(10));
            } else if (attr.start(11) > -1) {
                // STYLE inline attribute
                // then, parse for URIs
                this.numberOfLinksExtracted += ExtractorCSS.processStyleCode(
                    curi, value, getController());
                
            } else if (attr.start(12) > -1) {
                // METHOD
                method = value;
                // form processing finished at end (after ACTION also collected)
            } else if (attr.start(13) > -1) {
                if("NAME".equalsIgnoreCase(attrName.toString())) {
                    // remember 'name' for end-analysis
                    nameVal = value; 
                }
                if("FLASHVARS".equalsIgnoreCase(attrName.toString())) {
                    // consider FLASHVARS attribute immediately
                    valueContext = Link.elementContext(element,attr.group(13));
                    considerQueryStringValues(curi, value, valueContext,Link.SPECULATIVE_HOP);
                }
                // any other attribute
                // ignore for now
                // could probe for path- or script-looking strings, but
                // those should be vanishingly rare in other attributes,
                // and/or symptomatic of page bugs
            }
        }
        TextUtils.recycleMatcher(attr);

        // finish handling codebase/resources now that all available
        if (resources != null) {
            Iterator<String> iter = resources.iterator();
            UURI codebaseURI = null;
            String res = null;
            try {
                if (codebase != null) {
                    // TODO: Pass in the charset.
                    codebaseURI = UURIFactory.
                        getInstance(curi.getUURI(), codebase);
                }
                while(iter.hasNext()) {
                    res = iter.next().toString();
                    res = (String) TextUtils.unescapeHtml(res);
                    if (codebaseURI != null) {
                        res = codebaseURI.resolve(res).toString();
                    }
                    processEmbed(curi, res, element); // TODO: include attribute too
                }
            } catch (URIException e) {
                curi.addLocalizedError(getName(), e, "BAD CODEBASE " + codebase);
            } catch (IllegalArgumentException e) {
                DevUtils.logger.log(Level.WARNING, "processGeneralTag()\n" +
                    "codebase=" + codebase + " res=" + res + "\n" +
                    DevUtils.extraInfo(), e);
            }
        }
        
        // finish handling form action, now method is available
        if(action != null) {
            if(method == null || "GET".equalsIgnoreCase(method.toString()) 
                    || ! ((Boolean)getUncheckedAttribute(curi,
                            ATTR_EXTRACT_ONLY_FORM_GETS)).booleanValue()) {
                processLink(curi, action, actionContext);
            }
        }
        
        // finish handling VALUE
        if(valueVal != null) {
            if("PARAM".equalsIgnoreCase(elementStr) && "flashvars".equalsIgnoreCase(nameVal.toString())) {
                // special handling for <PARAM NAME='flashvars" VALUE="">
                String queryStringLike = valueVal.toString();
                // treat value as query-string-like "key=value[;key=value]*" pairings
                considerQueryStringValues(curi, queryStringLike, valueContext,Link.SPECULATIVE_HOP);
            } else {
                // regular VALUE handling
                if (extractValueAttributes) {
                    considerIfLikelyUri(curi,valueVal,valueContext,Link.NAVLINK_HOP);
                }
            }
        }
    }

    /**
     * Consider a query-string-like collections of key=value[;key=value]
     * pairs for URI-like strings in the values. Where URI-like strings are
     * found, add as discovered outlink. 
     * 
     * @param curi origin CrawlURI
     * @param queryString query-string-like string
     * @param valueContext page context where found
     */
    protected void considerQueryStringValues(CrawlURI curi,
            CharSequence queryString, CharSequence valueContext, char hopType) {
        for(String pairString : queryString.toString().split(";")) {
            String[] keyVal = pairString.split("=");
            if(keyVal.length==2) {
                considerIfLikelyUri(curi,keyVal[1],valueContext, hopType);
            }
        }
    }

    /**
     * Consider whether a given string is URI-like. If so, add as discovered 
     * outlink. 
     * 
     * @param curi origin CrawlURI
     * @param queryString query-string-like string
     * @param valueContext page context where found

     */
    protected void considerIfLikelyUri(CrawlURI curi, CharSequence candidate, 
            CharSequence valueContext, char hopType) {
        if(UriUtils.isLikelyUriHtmlContextLegacy(candidate)) {
            addLinkFromString(curi,candidate,valueContext,hopType);
        }
    }

    /**
     * Extract the (java)script source in the given CharSequence. 
     * 
     * @param curi source CrawlURI
     * @param cs CharSequence of javascript code
     */
    protected void processScriptCode(CrawlURI curi, CharSequence cs) {
        if((Boolean)getUncheckedAttribute(curi, ATTR_EXTRACT_JAVASCRIPT)) {
            this.numberOfLinksExtracted +=
                ExtractorJS.considerStrings(curi, cs, getController(), false);
        } // else do nothing
    }

    static final String JAVASCRIPT = "(?i)^javascript:.*";

    /**
     * Handle generic HREF cases.
     * 
     * @param curi
     * @param value
     * @param context
     */
    protected void processLink(CrawlURI curi, final CharSequence value,
            CharSequence context) {
        if (TextUtils.matches(JAVASCRIPT, value)) {
            processScriptCode(curi, value. subSequence(11, value.length()));
        } else {    
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("link: " + value.toString() + " from " + curi);
            }
            addLinkFromString(curi, value, context, Link.NAVLINK_HOP);
            this.numberOfLinksExtracted++;
        }
    }

    protected void addLinkFromString(CrawlURI curi, CharSequence uri,
            CharSequence context, char hopType) {
        try {
            // We do a 'toString' on context because its a sequence from
            // the underlying ReplayCharSequence and the link its about
            // to become a part of is expected to outlive the current
            // ReplayCharSequence.
            curi.createAndAddLinkRelativeToBase(uri.toString(), context.toString(),
                hopType);
        } catch (URIException e) {
            if (getController() != null) {
                getController().logUriError(e, curi.getUURI(), uri);
            } else {
                logger.info("Failed createAndAddLinkRelativeToBase " +
                    curi + ", " + uri + ", " + context + ", " + hopType +
                    ": " + e);
            }
        }
    }

    protected final void processEmbed(CrawlURI curi, CharSequence value,
            CharSequence context) {
        processEmbed(curi, value, context, Link.EMBED_HOP);
    }

    protected void processEmbed(CrawlURI curi, final CharSequence value,
            CharSequence context, char hopType) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("embed (" + hopType + "): " + value.toString() +
                " from " + curi);
        }
        addLinkFromString(curi,
            (value instanceof String)?
                (String)value: value.toString(),
            context, hopType);
        this.numberOfLinksExtracted++;
    }

    public void extract(CrawlURI curi) {
        if (!isHttpTransactionContentToProcess(curi) ||
                ! (isExpectedMimeType(curi.getContentType(), "text/html")
                   || isExpectedMimeType(curi.getContentType(), "application/xhtml")
                   || isExpectedMimeType(curi.getContentType(), "text/vnd.wap.wml")
                   || isExpectedMimeType(curi.getContentType(), "application/vnd.wap.wml")
                   || isExpectedMimeType(curi.getContentType(), "application/vnd.wap.xhtml"))) {
            return;
        }

        final boolean ignoreUnexpectedHTML =
             ((Boolean)getUncheckedAttribute(curi, 
                 ATTR_IGNORE_UNEXPECTED_HTML)).booleanValue();        

        if (ignoreUnexpectedHTML) {
            try {
                if(!isHtmlExpectedHere(curi)) {
                    // HTML was not expected (eg a GIF was expected) so ignore
                    // (as if a soft 404)
                    return;
                }
            } catch (URIException e) {
                logger.severe("Failed expectedHTML test: " + e.getMessage());
            }
        }

        this.numberOfCURIsHandled++;

        ReplayCharSequence cs = null;
        
        try {
           HttpRecorder hr = curi.getHttpRecorder();
           if (hr == null) {
               throw new IOException("Why is recorder null here?");
           }
           cs = hr.getReplayCharSequence();
        } catch (IOException e) {
            curi.addLocalizedError(this.getName(), e,
                "Failed get of replay char sequence " + curi.toString() +
                    " " + e.getMessage());
            logger.log(Level.SEVERE,"Failed get of replay char sequence in " +
                Thread.currentThread().getName(), e);
        }
        
        if (cs == null) {
            return;
        }

        // We have a ReplayCharSequence open.  Wrap all in finally so we
        // for sure close it before we leave.
        try {
            // Extract all links from the charsequence
            extract(curi, cs);
            // Set flag to indicate that link extraction is completed.
            curi.linkExtractorFinished();
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (IOException ioe) {
                    logger.warning(TextUtils.exceptionToString(
                        "Failed close of ReplayCharSequence.", ioe));
                }
            }
        }
    }

    /**
     * Run extractor.
     * This method is package visible to ease testing.
     * @param curi CrawlURI we're processing.
     * @param cs Sequence from underlying ReplayCharSequence. This
     * is TRANSIENT data. Make a copy if you want the data to live outside
     * of this extractors' lifetime.
     */
    void extract(CrawlURI curi, CharSequence cs) {
        Matcher tags = TextUtils.getMatcher(RELEVANT_TAG_EXTRACTOR, cs);
        while(tags.find()) {
            if(Thread.interrupted()){
                break;
            }
            if (tags.start(8) > 0) {
                // comment match
                // for now do nothing
            } else if (tags.start(7) > 0) {
                // <meta> match
                int start = tags.start(5);
                int end = tags.end(5);
                assert start >= 0: "Start is: " + start + ", " + curi;
                assert end >= 0: "End is :" + end + ", " + curi;
                if (processMeta(curi,
                    cs.subSequence(start, end))) {

                    // meta tag included NOFOLLOW; abort processing
                    break;
                }
            } else if (tags.start(5) > 0) {
                // generic <whatever> match
                int start5 = tags.start(5);
                int end5 = tags.end(5);
                assert start5 >= 0: "Start is: " + start5 + ", " + curi;
                assert end5 >= 0: "End is :" + end5 + ", " + curi;
                int start6 = tags.start(6);
                int end6 = tags.end(6);
                assert start6 >= 0: "Start is: " + start6 + ", " + curi;
                assert end6 >= 0: "End is :" + end6 + ", " + curi;
                processGeneralTag(curi,
                    cs.subSequence(start6, end6),
                    cs.subSequence(start5, end5));

            } else if (tags.start(1) > 0) {
                // <script> match
                int start = tags.start(1);
                int end = tags.end(1);
                assert start >= 0: "Start is: " + start + ", " + curi;
                assert end >= 0: "End is :" + end + ", " + curi;
                assert tags.end(2) >= 0: "Tags.end(2) illegal " + tags.end(2) +
                    ", " + curi;
                processScript(curi, cs.subSequence(start, end),
                    tags.end(2) - start);

            } else if (tags.start(3) > 0){
                // <style... match
                int start = tags.start(3);
                int end = tags.end(3);
                assert start >= 0: "Start is: " + start + ", " + curi;
                assert end >= 0: "End is :" + end + ", " + curi;
                assert tags.end(4) >= 0: "Tags.end(4) illegal " + tags.end(4) +
                    ", " + curi;
                processStyle(curi, cs.subSequence(start, end),
                    tags.end(4) - start);
            }
        }
        TextUtils.recycleMatcher(tags);
    }


    static final String NON_HTML_PATH_EXTENSION =
        "(?i)(gif)|(jp(e)?g)|(png)|(tif(f)?)|(bmp)|(avi)|(mov)|(mp(e)?g)"+
        "|(mp3)|(mp4)|(swf)|(wav)|(au)|(aiff)|(mid)";

    /**
     * Test whether this HTML is so unexpected (eg in place of a GIF URI)
     * that it shouldn't be scanned for links.
     *
     * @param curi CrawlURI to examine.
     * @return True if HTML is acceptable/expected here
     * @throws URIException
     */
    protected boolean isHtmlExpectedHere(CrawlURI curi) throws URIException {
        String path = curi.getUURI().getPath();
        if(path==null) {
            // no path extension, HTML is fine
            return true;
        }
        int dot = path.lastIndexOf('.');
        if (dot < 0) {
            // no path extension, HTML is fine
            return true;
        }
        if(dot<(path.length()-5)) {
            // extension too long to recognize, HTML is fine
            return true;
        }
        String ext = path.substring(dot+1);
        return ! TextUtils.matches(NON_HTML_PATH_EXTENSION, ext);
    }

    protected void processScript(CrawlURI curi, CharSequence sequence,
            int endOfOpenTag) {
        // first, get attributes of script-open tag
        // as per any other tag
        processGeneralTag(curi,sequence.subSequence(0,6),
            sequence.subSequence(0,endOfOpenTag));

        // then, apply best-effort string-analysis heuristics
        // against any code present (false positives are OK)
        processScriptCode(
            curi, sequence.subSequence(endOfOpenTag, sequence.length()));
    }

    /**
     * Process metadata tags.
     * @param curi CrawlURI we're processing.
     * @param cs Sequence from underlying ReplayCharSequence. This
     * is TRANSIENT data. Make a copy if you want the data to live outside
     * of this extractors' lifetime.
     * @return True robots exclusion metatag.
     */
    protected boolean processMeta(CrawlURI curi, CharSequence cs) {
        Matcher attr = TextUtils.getMatcher(EACH_ATTRIBUTE_EXTRACTOR, cs);
        String name = null;
        String httpEquiv = null;
        String content = null;
        while (attr.find()) {
            int valueGroup =
                (attr.start(14) > -1) ? 14 : (attr.start(15) > -1) ? 15 : 16;
            CharSequence value =
                cs.subSequence(attr.start(valueGroup), attr.end(valueGroup));
            value = TextUtils.unescapeHtml(value);
            if (attr.group(1).equalsIgnoreCase("name")) {
                name = value.toString();
            } else if (attr.group(1).equalsIgnoreCase("http-equiv")) {
                httpEquiv = value.toString();
            } else if (attr.group(1).equalsIgnoreCase("content")) {
                content = value.toString();
            }
            // TODO: handle other stuff
        }
        TextUtils.recycleMatcher(attr);

        // Look for the 'robots' meta-tag
        if("robots".equalsIgnoreCase(name) && content != null ) {
            curi.putString(A_META_ROBOTS, content);
            RobotsHonoringPolicy policy =
                getSettingsHandler().getOrder().getRobotsHonoringPolicy();
            String contentLower = content.toLowerCase();
            if ((policy == null
                || (!policy.isType(curi, RobotsHonoringPolicy.IGNORE)
                    && !policy.isType(curi, RobotsHonoringPolicy.CUSTOM)))
                && (contentLower.indexOf("nofollow") >= 0
                    || contentLower.indexOf("none") >= 0)) {
                // if 'nofollow' or 'none' is specified and the
                // honoring policy is not IGNORE or CUSTOM, end html extraction
                logger.fine("HTML extraction skipped due to robots meta-tag for: "
                                + curi.toString());
                return true;
            }
        } else if ("refresh".equalsIgnoreCase(httpEquiv) && content != null) {
            int urlIndex = content.indexOf("=") + 1;
            if(urlIndex>0) {
                String refreshUri = content.substring(urlIndex);
                try {
                    curi.createAndAddLinkRelativeToBase(refreshUri, "meta",
                        Link.REFER_HOP);
                } catch (URIException e) {
                    if (getController() != null) {
                        getController().logUriError(e, curi.getUURI(), refreshUri);
                    } else {
                        logger.info("Failed createAndAddLinkRelativeToBase " +
                            curi + ", " + cs + ", " + refreshUri + ": " + e);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Process style text.
     * @param curi CrawlURI we're processing.
     * @param sequence Sequence from underlying ReplayCharSequence. This
     * is TRANSIENT data. Make a copy if you want the data to live outside
     * of this extractors' lifetime.
     * @param endOfOpenTag
     */
    protected void processStyle(CrawlURI curi, CharSequence sequence,
            int endOfOpenTag) {
        // First, get attributes of script-open tag as per any other tag.
        processGeneralTag(curi, sequence.subSequence(0,6),
            sequence.subSequence(0,endOfOpenTag));

        // then, parse for URIs
        this.numberOfLinksExtracted += ExtractorCSS.processStyleCode(
            curi, sequence.subSequence(endOfOpenTag,sequence.length()),
                getController());
    }
    


    /* (non-Javadoc)
     * @see org.archive.crawler.framework.Processor#report()
     */
    public String report() {
        StringBuffer ret = new StringBuffer();
        ret.append("Processor: org.archive.crawler.extractor.ExtractorHTML\n");
        ret.append("  Function:          Link extraction on HTML documents\n");
        ret.append("  CrawlURIs handled: " + this.numberOfCURIsHandled + "\n");
        ret.append("  Links extracted:   " + this.numberOfLinksExtracted +
            "\n\n");
        return ret.toString();
    }
}

