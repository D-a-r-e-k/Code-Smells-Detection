/**
     * Receive notification of ignorable whitespace in element content.
     * 
     * Not sure how to get this invoked quite yet.
     * 
     * @param ch The characters from the XML document.
     * @param start The start position in the array.
     * @param length The number of characters to read from the array.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #characters
     * 
     * @throws org.xml.sax.SAXException
     */
public void ignorableWhitespace(char ch[], int start, int length) throws org.xml.sax.SAXException {
    if (0 == length)
        return;
    characters(ch, start, length);
}
