// startDocumentEntity(XMLInputSource)  
/**
     * Starts the DTD entity. The DTD entity has the "[dtd]"
     * pseudo-name.
     *
     * @param xmlInputSource The input source of the DTD entity.
     *
     * @throws IOException  Thrown on i/o error.
     * @throws XNIException Thrown by entity handler to signal an error.
     */
public void startDTDEntity(XMLInputSource xmlInputSource) throws IOException, XNIException {
    startEntity(DTDEntity, xmlInputSource, false, true);
}
