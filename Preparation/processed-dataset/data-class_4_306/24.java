// elementDecl(String,String)  
/**
     * The start of an attribute list.
     * 
     * @param elementName The name of the element that this attribute
     *                    list is associated with.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startAttlist(String elementName, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.startAttlist(elementName, augs);
    if (fDTDHandler != null) {
        fDTDHandler.startAttlist(elementName, augs);
    }
}
