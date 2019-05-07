/**
     * This method notifies the end of a general entity.
     * <p>
     * <strong>Note:</strong> This method is not called for entity references
     * appearing as part of attribute values.
     * 
     * @param name   The name of the entity.
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.endGeneralEntity(name, augs);
    }
}
