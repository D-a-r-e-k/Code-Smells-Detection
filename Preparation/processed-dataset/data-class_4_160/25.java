/**
     *  This helper method adds all the input fields to your editor that the SpamFilter requires
     *  to check for spam.  This <i>must</i> be in your editor form if you intend to use
     *  the SpamFilter.
     *  
     *  @param pageContext The PageContext
     *  @return A HTML string which contains input fields for the SpamFilter.
     */
public static final String insertInputFields(PageContext pageContext) {
    WikiContext ctx = WikiContext.findContext(pageContext);
    WikiEngine engine = ctx.getEngine();
    StringBuffer sb = new StringBuffer();
    if (engine.getContentEncoding().equals("UTF-8")) {
        sb.append("<input name='encodingcheck' type='hidden' value='ぁ' />\n");
    }
    return sb.toString();
}
