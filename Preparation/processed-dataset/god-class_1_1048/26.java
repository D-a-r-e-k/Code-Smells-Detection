/**
     * Need need to get rid of any user-visible HTML tags once all text has been
     * removed such as &lt;BR&gt;. This sounds like a better approach than removing
     * all HTML tags and taking the chance to leave some tags un-closed.
     *
     * WARNING: this method has serious performance problems a
     *
     * @author Alexis Moussine-Pouchkine (alexis.moussine-pouchkine@france.sun.com)
     * @author Lance Lavandowska
     * @param str the String object to modify
     * @return the new String object without the HTML "visible" tags
     */
private static String removeVisibleHTMLTags(String str) {
    str = stripLineBreaks(str);
    StringBuffer result = new StringBuffer(str);
    StringBuffer lcresult = new StringBuffer(str.toLowerCase());
    // <img should take care of smileys 
    String[] visibleTags = { "<img" };
    // are there others to add? 
    int stringIndex;
    for (int j = 0; j < visibleTags.length; j++) {
        while ((stringIndex = lcresult.indexOf(visibleTags[j])) != -1) {
            if (visibleTags[j].endsWith(">")) {
                result.delete(stringIndex, stringIndex + visibleTags[j].length());
                lcresult.delete(stringIndex, stringIndex + visibleTags[j].length());
            } else {
                // need to delete everything up until next closing '>', for <img for instance 
                int endIndex = result.indexOf(">", stringIndex);
                if (endIndex > -1) {
                    // only delete it if we find the end!  If we don't the HTML may be messed up, but we 
                    // can't safely delete anything. 
                    result.delete(stringIndex, endIndex + 1);
                    lcresult.delete(stringIndex, endIndex + 1);
                }
            }
        }
    }
    // TODO:  This code is buggy by nature.  It doesn't deal with nesting of tags properly. 
    // remove certain elements with open & close tags 
    String[] openCloseTags = { "li", "a", "div", "h1", "h2", "h3", "h4" };
    // more ? 
    for (int j = 0; j < openCloseTags.length; j++) {
        // could this be better done with a regular expression? 
        String closeTag = "</" + openCloseTags[j] + ">";
        int lastStringIndex = 0;
        while ((stringIndex = lcresult.indexOf("<" + openCloseTags[j], lastStringIndex)) > -1) {
            lastStringIndex = stringIndex;
            // Try to find the matching closing tag  (ignores possible nesting!) 
            int endIndex = lcresult.indexOf(closeTag, stringIndex);
            if (endIndex > -1) {
                // If we found it delete it. 
                result.delete(stringIndex, endIndex + closeTag.length());
                lcresult.delete(stringIndex, endIndex + closeTag.length());
            } else {
                // Try to see if it is a self-closed empty content tag, i.e. closed with />. 
                endIndex = lcresult.indexOf(">", stringIndex);
                int nextStart = lcresult.indexOf("<", stringIndex + 1);
                if (endIndex > stringIndex && lcresult.charAt(endIndex - 1) == '/' && (endIndex < nextStart || nextStart == -1)) {
                    // Looks like it, so remove it. 
                    result.delete(stringIndex, endIndex + 1);
                    lcresult.delete(stringIndex, endIndex + 1);
                }
            }
        }
    }
    return result.toString();
}
