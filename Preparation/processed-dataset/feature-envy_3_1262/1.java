// setInputSource(XMLInputSource) 
/**
     * Parses the document in a pull parsing fashion.
     *
     * @param complete True if the pull parser should parse the
     *                 remaining document completely.
     *
     * @return True if there is more document to parse.
     *
     * @exception XNIException Any XNI exception, possibly wrapping 
     *                         another exception.
     * @exception IOException  An IO exception from the parser, possibly
     *                         from a byte stream or character stream
     *                         supplied by the parser.
     *
     * @see #setInputSource
     */
public boolean parse(boolean complete) throws XNIException, IOException {
    try {
        boolean more = fDocumentScanner.scanDocument(complete);
        if (!more) {
            cleanup();
        }
        return more;
    } catch (XNIException e) {
        cleanup();
        throw e;
    } catch (IOException e) {
        cleanup();
        throw e;
    }
}
