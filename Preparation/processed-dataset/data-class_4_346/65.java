/**
     * Adds a URI/LocalName pair of strings to the list.
     *
     * @param URI_and_localName String of the form "{uri}local" or "local" 
     * 
     * @return a QName object
     */
private void addCdataSectionElement(String URI_and_localName, Vector v) {
    StringTokenizer tokenizer = new StringTokenizer(URI_and_localName, "{}", false);
    String s1 = tokenizer.nextToken();
    String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
    if (null == s2) {
        // add null URI and the local name  
        v.addElement(null);
        v.addElement(s1);
    } else {
        // add URI, then local name  
        v.addElement(s1);
        v.addElement(s2);
    }
}
