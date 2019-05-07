// attributeDecl(String,String,String,String[],String,XMLString, XMLString, Augmentations)  
/**
     * The end of an attribute list.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endAttlist(Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.endAttlist(augs);
    if (fDTDHandler != null) {
        fDTDHandler.endAttlist(augs);
    }
}
