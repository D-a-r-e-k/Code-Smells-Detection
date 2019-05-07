void ensurePrefixIsDeclared(String ns, String rawName) throws org.xml.sax.SAXException {
    if (ns != null && ns.length() > 0) {
        int index;
        final boolean no_prefix = ((index = rawName.indexOf(":")) < 0);
        String prefix = (no_prefix) ? "" : rawName.substring(0, index);
        if (null != prefix) {
            String foundURI = m_prefixMap.lookupNamespace(prefix);
            if ((null == foundURI) || !foundURI.equals(ns)) {
                this.startPrefixMapping(prefix, ns);
                // Bugzilla1133: Generate attribute as well as namespace event.  
                // SAX does expect both.  
                this.addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, // local name  
                no_prefix ? "xmlns" : ("xmlns:" + prefix), // qname  
                "CDATA", ns, false);
            }
        }
    }
}
