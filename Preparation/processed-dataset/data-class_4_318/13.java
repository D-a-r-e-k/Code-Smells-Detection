// xmlDecl(String,String,String)  
/**
     * Notifies of the presence of the DOCTYPE line in the document.
     *
     * @param rootElement The name of the root element.
     * @param publicId    The public identifier if an external DTD or null
     *                    if the external DTD is specified using SYSTEM.
     * @param systemId    The system identifier if an external DTD, null
     *                    otherwise.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
    }
}
