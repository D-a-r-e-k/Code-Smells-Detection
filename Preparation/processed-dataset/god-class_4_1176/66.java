/**
     * Remembers the cdata sections specified in the cdata-section-elements.
     * The "official way to set URI and localName pairs. 
     * This method should be used by both Xalan and XSLTC.
     * 
     * @param URI_and_localNames a vector of pairs of Strings (URI/local)
     */
public void setCdataSectionElements(Vector URI_and_localNames) {
    // convert to the new way.  
    if (URI_and_localNames != null) {
        final int len = URI_and_localNames.size() - 1;
        if (len > 0) {
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len; i += 2) {
                // whitspace separated "{uri1}local1 {uri2}local2 ..."  
                if (i != 0)
                    sb.append(' ');
                final String uri = (String) URI_and_localNames.elementAt(i);
                final String localName = (String) URI_and_localNames.elementAt(i + 1);
                if (uri != null) {
                    // If there is no URI don't put this in, just the localName then.  
                    sb.append('{');
                    sb.append(uri);
                    sb.append('}');
                }
                sb.append(localName);
            }
            m_StringOfCDATASections = sb.toString();
        }
    }
    initCdataElems(m_StringOfCDATASections);
}
