/**
     * An element declaration.
     * 
     * @param name         The name of the element.
     * @param contentModel The element content model.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void elementDecl(String name, String contentModel, Augmentations augs) throws XNIException {
    //check VC: Unique Element Declaration  
    if (fValidation) {
        if (fDTDElementDecls.contains(name)) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_ELEMENT_ALREADY_DECLARED", new Object[] { name }, XMLErrorReporter.SEVERITY_ERROR);
        } else {
            fDTDElementDecls.add(name);
        }
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.elementDecl(name, contentModel, augs);
    if (fDTDHandler != null) {
        fDTDHandler.elementDecl(name, contentModel, augs);
    }
}
