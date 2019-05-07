/**
     * Begin the scope of a prefix-URI Namespace mapping
     * just before another element is about to start.
     * This call will close any open tags so that the prefix mapping
     * will not apply to the current element, but the up comming child.
     * 
     * @see org.xml.sax.ContentHandler#startPrefixMapping
     * 
     * @param prefix The Namespace prefix being declared.
     * @param uri The Namespace URI the prefix is mapped to.
     * 
     * @throws org.xml.sax.SAXException The client may throw
     *            an exception during processing.
     * 
     */
public void startPrefixMapping(String prefix, String uri) throws org.xml.sax.SAXException {
    // the "true" causes the flush of any open tags  
    startPrefixMapping(prefix, uri, true);
}
