// parse(String,DocumentFragment) 
/** Parses a document fragment. */
public void parse(InputSource source, DocumentFragment fragment) throws SAXException, IOException {
    fCurrentNode = fDocumentFragment = fragment;
    fDocument = fDocumentFragment.getOwnerDocument();
    try {
        String pubid = source.getPublicId();
        String sysid = source.getSystemId();
        String encoding = source.getEncoding();
        InputStream stream = source.getByteStream();
        Reader reader = source.getCharacterStream();
        XMLInputSource inputSource = new XMLInputSource(pubid, sysid, sysid);
        inputSource.setEncoding(encoding);
        inputSource.setByteStream(stream);
        inputSource.setCharacterStream(reader);
        fParserConfiguration.parse(inputSource);
    } catch (XMLParseException e) {
        Exception ex = e.getException();
        if (ex != null) {
            throw new SAXParseException(e.getMessage(), null, ex);
        }
        throw new SAXParseException(e.getMessage(), null);
    }
}
