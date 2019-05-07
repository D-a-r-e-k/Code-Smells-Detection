// endEntity(String)  
/**
     * Notifies of the presence of a TextDecl line in an entity. If present,
     * this method will be called immediately following the startParameterEntity call.
     * <p>
     * <strong>Note:</strong> This method is only called for external
     * parameter entities referenced in the DTD.
     * 
     * @param version  The XML version, or null if not specified.
     * @param encoding The IANA encoding name of the entity.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.textDecl(version, encoding, augs);
    }
}
