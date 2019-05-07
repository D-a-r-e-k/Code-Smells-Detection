/**
     * Receive notification of the end of an element.
     * @param name The element type name
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *     wrapping another exception.
     */
public void endElement(String name) throws org.xml.sax.SAXException {
    endElement(null, null, name);
}
