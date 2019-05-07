// startEntity(String,boolean)  
/**
     * Starts the document entity. The document entity has the "[xml]"
     * pseudo-name.
     *
     * @param xmlInputSource The input source of the document entity.
     *
     * @throws IOException  Thrown on i/o error.
     * @throws XNIException Thrown by entity handler to signal an error.
     */
public void startDocumentEntity(XMLInputSource xmlInputSource) throws IOException, XNIException {
    startEntity(XMLEntity, xmlInputSource, false, true);
}
