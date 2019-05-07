/**
     * Remembers the cdata sections specified in the cdata-section-elements by appending the given
     * cdata section elements to the list. This method can be called multiple times, but once an
     * element is put in the list of cdata section elements it can not be removed.
     * This method should be used by both Xalan and XSLTC.
     * 
     * @param URI_and_localNames a whitespace separated list of element names, each element
     * is a URI in curly braces (optional) and a local name. An example of such a parameter is:
     * "{http://company.com}price {myURI2}book chapter"
     */
public void addCdataSectionElements(String URI_and_localNames) {
    if (URI_and_localNames != null)
        initCdataElems(URI_and_localNames);
    if (m_StringOfCDATASections == null)
        m_StringOfCDATASections = URI_and_localNames;
    else
        m_StringOfCDATASections += (" " + URI_and_localNames);
}
