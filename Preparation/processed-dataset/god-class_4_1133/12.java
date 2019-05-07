//  
// XMLEntityHandler methods  
//  
/**
     * This method notifies of the start of an entity. The DTD has the
     * pseudo-name of "[dtd]" parameter entity names start with '%'; and
     * general entities are just specified by their name.
     * 
     * @param name     The name of the entity.
     * @param identifier The resource identifier.
     * @param encoding The auto-detected IANA encoding name of the entity
     *                 stream. This value will be null in those situations
     *                 where the entity encoding is not auto-detected (e.g.
     *                 internal entities or a document entity that is
     *                 parsed from a java.io.Reader).
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
    // keep track of this entity before fEntityDepth is increased  
    if (fEntityDepth == fEntityStack.length) {
        int[] entityarray = new int[fEntityStack.length * 2];
        System.arraycopy(fEntityStack, 0, entityarray, 0, fEntityStack.length);
        fEntityStack = entityarray;
    }
    fEntityStack[fEntityDepth] = fMarkupDepth;
    super.startEntity(name, identifier, encoding, augs);
    // WFC:  entity declared in external subset in standalone doc  
    if (fStandalone && fEntityManager.isEntityDeclInExternalSubset(name)) {
        reportFatalError("MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { name });
    }
    // call handler  
    if (fDocumentHandler != null && !fScanningAttribute) {
        if (!name.equals("[xml]")) {
            fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
        }
    }
}
