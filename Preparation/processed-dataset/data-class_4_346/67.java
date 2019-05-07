/**
     * Makes sure that the namespace URI for the given qualified attribute name
     * is declared.
     * @param ns the namespace URI
     * @param rawName the qualified name 
     * @return returns null if no action is taken, otherwise it returns the
     * prefix used in declaring the namespace. 
     * @throws SAXException
     */
protected String ensureAttributesNamespaceIsDeclared(String ns, String localName, String rawName) throws org.xml.sax.SAXException {
    if (ns != null && ns.length() > 0) {
        // extract the prefix in front of the raw name  
        int index = 0;
        String prefixFromRawName = (index = rawName.indexOf(":")) < 0 ? "" : rawName.substring(0, index);
        if (index > 0) {
            // we have a prefix, lets see if it maps to a namespace   
            String uri = m_prefixMap.lookupNamespace(prefixFromRawName);
            if (uri != null && uri.equals(ns)) {
                // the prefix in the raw name is already maps to the given namespace uri  
                // so we don't need to do anything  
                return null;
            } else {
                // The uri does not map to the prefix in the raw name,  
                // so lets make the mapping.  
                this.startPrefixMapping(prefixFromRawName, ns, false);
                this.addAttribute("http://www.w3.org/2000/xmlns/", prefixFromRawName, "xmlns:" + prefixFromRawName, "CDATA", ns, false);
                return prefixFromRawName;
            }
        } else {
            // we don't have a prefix in the raw name.  
            // Does the URI map to a prefix already?  
            String prefix = m_prefixMap.lookupPrefix(ns);
            if (prefix == null) {
                // uri is not associated with a prefix,  
                // so lets generate a new prefix to use  
                prefix = m_prefixMap.generateNextPrefix();
                this.startPrefixMapping(prefix, ns, false);
                this.addAttribute("http://www.w3.org/2000/xmlns/", prefix, "xmlns:" + prefix, "CDATA", ns, false);
            }
            return prefix;
        }
    }
    return null;
}
