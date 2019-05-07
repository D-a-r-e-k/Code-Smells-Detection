void method0() { 
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
public static final char NAVLINK_HOP = 'L';
// TODO: change to 'N' to avoid 'L'ink confusion? 
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
}
