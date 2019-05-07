// getDTDContentModelSource():  XMLDTDContentModelSource  
/**
     * The start of a content model. Depending on the type of the content
     * model, specific methods may be called between the call to the
     * startContentModel method and the call to the endContentModel method.
     * 
     * @param elementName The name of the element.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startContentModel(String elementName, Augmentations augs) throws XNIException {
    if (fValidation) {
        fDTDElementDeclName = elementName;
        fMixedElementTypes.clear();
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.startContentModel(elementName, augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.startContentModel(elementName, augs);
    }
}
