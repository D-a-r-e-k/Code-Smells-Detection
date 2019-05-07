/**
     * Handle a prefix/uri mapping, which is associated with a startElement()
     * that is soon to follow. Need to close any open start tag to make
     * sure than any name space attributes due to this event are associated wih
     * the up comming element, not the current one.
     * @see ExtendedContentHandler#startPrefixMapping
     *
     * @param prefix The Namespace prefix being declared.
     * @param uri The Namespace URI the prefix is mapped to.
     * @param shouldFlush true if any open tags need to be closed first, this
     * will impact which element the mapping applies to (open parent, or its up
     * comming child)
     * @return returns true if the call made a change to the current 
     * namespace information, false if it did not change anything, e.g. if the
     * prefix/namespace mapping was already in scope from before.
     * 
     * @throws org.xml.sax.SAXException The client may throw
     *            an exception during processing.
     *
     *
     */
public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws org.xml.sax.SAXException {
    /* Remember the mapping, and at what depth it was declared
         * This is one greater than the current depth because these
         * mappings will apply to the next depth. This is in
         * consideration that startElement() will soon be called
         */
    boolean pushed;
    int pushDepth;
    if (shouldFlush) {
        flushPending();
        // the prefix mapping applies to the child element (one deeper)  
        pushDepth = m_elemContext.m_currentElemDepth + 1;
    } else {
        // the prefix mapping applies to the current element  
        pushDepth = m_elemContext.m_currentElemDepth;
    }
    pushed = m_prefixMap.pushNamespace(prefix, uri, pushDepth);
    if (pushed) {
        /* Brian M.: don't know if we really needto do this. The
             * callers of this object should have injected both
             * startPrefixMapping and the attributes.  We are 
             * just covering our butt here.
             */
        String name;
        if (EMPTYSTRING.equals(prefix)) {
            name = "xmlns";
            addAttributeAlways(XMLNS_URI, name, name, "CDATA", uri, false);
        } else {
            if (!EMPTYSTRING.equals(uri)) // hack for XSLTC attribset16 test  
            {
                // that maps ns1 prefix to "" URI  
                name = "xmlns:" + prefix;
                /* for something like xmlns:abc="w3.pretend.org"
                     *  the      uri is the value, that is why we pass it in the
                     * value, or 5th slot of addAttributeAlways()
                     */
                addAttributeAlways(XMLNS_URI, prefix, name, "CDATA", uri, false);
            }
        }
    }
    return pushed;
}
