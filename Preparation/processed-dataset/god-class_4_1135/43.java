// pcdata()  
/**
     * A referenced element in a mixed or children content model.
     * 
     * @param elementName The name of the referenced element.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void element(String elementName, Augmentations augs) throws XNIException {
    // check VC: No duplicate Types, in a single mixed-content declaration  
    if (fMixed && fValidation) {
        if (fMixedElementTypes.contains(elementName)) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "DuplicateTypeInMixedContent", new Object[] { fDTDElementDeclName, elementName }, XMLErrorReporter.SEVERITY_ERROR);
        } else {
            fMixedElementTypes.add(elementName);
        }
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.element(elementName, augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.element(elementName, augs);
    }
}
