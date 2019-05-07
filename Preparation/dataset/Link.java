/* Link
*
* $Id: Link.java 6773 2010-02-18 01:52:41Z szznax $
*
* Created on Mar 7, 2005
*
* Copyright (C) 2005 Internet Archive.
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
*/ 
package org.archive.crawler.extractor;

import java.io.Serializable;


/**
 * Link represents one discovered "edge" of the web graph: the source
 * URI, the destination URI, and the type of reference (represented by the
 * context in which it was found). 
 * 
 * As such, it is a suitably generic item to returned from generic 
 * link-extraction utility code.
 * 
 * @author gojomo
 */
public class Link implements Serializable {

    private static final long serialVersionUID = 7660959085498739376L;

    /* contexts for when another syntax (XPath-like or header-based)
     *  in unavailable */
    /** stand-in value for embeds without other context */
    public static final String EMBED_MISC = "=EMBED_MISC".intern();
    /** stand-in value for js-discovered urls without other context */
    public static final String JS_MISC = "=JS_MISC".intern();
    /** stand-in value for navlink urls without other context */
    public static final String NAVLINK_MISC = "=NAVLINK_MISC".intern();
    /** stand-in value for speculative/aggressively extracted urls without other context */
    public static final String SPECULATIVE_MISC = "=SPECULATIVE_MISC".intern();
    /** stand-in value for prerequisite without other context */
    public static final String PREREQ_MISC = "=PREREQ_MISC".intern(); 
    
    /* hop types */
    /** navigation links, like A/@HREF */
    public static final char NAVLINK_HOP = 'L'; // TODO: change to 'N' to avoid 'L'ink confusion?
    /** implied prerequisite links, like dns or robots */
    public static final char PREREQ_HOP = 'P';
    /** embedded links necessary to render the page, like IMG/@SRC */
    public static final char EMBED_HOP = 'E';
    /** speculative/aggressively extracted links, perhaps embed or nav, as in javascript */
    public static final char SPECULATIVE_HOP = 'X';
    /** referral/redirect links, like header 'Location:' on a 301/302 response */
    public static final char REFER_HOP = 'R';

    /** URI where this Link was discovered */
    private CharSequence source;
    /** URI (absolute) where this Link points */
    private CharSequence destination;
    /** context of discovery -- will be an XPath-like element[/@attribute] 
     * fragment for HTML URIs, a header name with trailing ':' for header 
     * values, or one of the stand-in constants when other context is 
     * unavailable */
    private CharSequence context;
    /** hop-type, as character abbrieviation */
    private char hopType;
    
    /**
     * Create a Link with the given fields.
     * @param source
     * @param destination
     * @param context
     * @param hopType
     */
    public Link(CharSequence source, CharSequence destination,
            CharSequence context, char hopType) {
        super();
        this.source = source;
        this.destination = destination;
        this.context = context;
        this.hopType = hopType;
    }

    /**
     * @return Returns the context.
     */
    public CharSequence getContext() {
        return context;
    }
    /**
     * @return Returns the destination.
     */
    public CharSequence getDestination() {
        return destination;
    }
    /**
     * @return Returns the source.
     */
    public CharSequence getSource() {
        return source;
    }

    /**
     * @return char hopType
     */
    public char getHopType() {
        return hopType;
    }

    /**
     * Create a suitable XPath-like context from an element name and optional
     * attribute name. 
     * 
     * @param element
     * @param attribute
     * @return CharSequence context
     */
    public static CharSequence elementContext(CharSequence element, CharSequence attribute) {
        return attribute == null? "": element + "/@" + attribute;
    }
    
    @Override
    public String toString() {
        return this.destination + " " + this.hopType + " " + this.context;
    }
}
