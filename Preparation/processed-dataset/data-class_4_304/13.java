// startEntity(String,XMLResourceIdentifier,String)  
/**
     * This method notifies the end of an entity. The DTD has the pseudo-name
     * of "[dtd]" parameter entity names start with '%'; and general entities 
     * are just specified by their name.
     * 
     * @param name The name of the entity.
     * @param augs Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endEntity(String name, Augmentations augs) throws XNIException {
    // flush possible pending output buffer - see scanContent  
    if (fInScanContent && fStringBuffer.length != 0 && fDocumentHandler != null) {
        fDocumentHandler.characters(fStringBuffer, null);
        fStringBuffer.length = 0;
    }
    super.endEntity(name, augs);
    // make sure markup is properly balanced  
    if (fMarkupDepth != fEntityStack[fEntityDepth]) {
        reportFatalError("MarkupEntityMismatch", null);
    }
    // call handler  
    if (fDocumentHandler != null && !fScanningAttribute) {
        if (!name.equals("[xml]")) {
            fDocumentHandler.endGeneralEntity(name, augs);
        }
    }
}
