/**
     * Searches for the list of qname properties with the specified key in the
     * property list. If the key is not found in this property list, the default
     * property list, and its defaults, recursively, are then checked. The
     * method returns <code>null</code> if the property is not found.
     *
     * @param   key   the property key.
     * @param props the list of properties to search in.
     * 
     * Sets the vector of local-name/URI pairs of the cdata section elements
     * specified in the cdata-section-elements property.
     * 
     * This method is essentially a copy of getQNameProperties() from
     * OutputProperties. Eventually this method should go away and a call
     * to setCdataSectionElements(Vector v) should be made directly.
     */
private void setCdataSectionElements(String key, Properties props) {
    String s = props.getProperty(key);
    if (null != s) {
        // Vector of URI/LocalName pairs  
        Vector v = new Vector();
        int l = s.length();
        boolean inCurly = false;
        StringBuffer buf = new StringBuffer();
        // parse through string, breaking on whitespaces.  I do this instead  
        // of a tokenizer so I can track whitespace inside of curly brackets,  
        // which theoretically shouldn't happen if they contain legal URLs.  
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!inCurly) {
                    if (buf.length() > 0) {
                        addCdataSectionElement(buf.toString(), v);
                        buf.setLength(0);
                    }
                    continue;
                }
            } else if ('{' == c)
                inCurly = true;
            else if ('}' == c)
                inCurly = false;
            buf.append(c);
        }
        if (buf.length() > 0) {
            addCdataSectionElement(buf.toString(), v);
            buf.setLength(0);
        }
        // call the official, public method to set the collected names  
        setCdataSectionElements(v);
    }
}
