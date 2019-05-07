/**
     * Receive notification of a skipped entity.
     * @see org.xml.sax.ContentHandler#skippedEntity
     * 
     * @param name The name of the skipped entity.  If it is a
     *       parameter                   entity, the name will begin with '%',
     * and if it is the external DTD subset, it will be the string
     * "[dtd]".
     * @throws org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     */
public void skippedEntity(String name) throws org.xml.sax.SAXException {
}
