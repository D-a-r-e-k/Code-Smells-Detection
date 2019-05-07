// scanStartElement():ename 
/**
         * Removes all spaces for the string (remember: JDK 1.3!)
         */
private String removeSpaces(final String content) {
    StringBuffer sb = null;
    for (int i = content.length() - 1; i >= 0; --i) {
        if (Character.isWhitespace(content.charAt(i))) {
            if (sb == null) {
                sb = new StringBuffer(content);
            }
            sb.deleteCharAt(i);
        }
    }
    return (sb == null) ? content : sb.toString();
}
