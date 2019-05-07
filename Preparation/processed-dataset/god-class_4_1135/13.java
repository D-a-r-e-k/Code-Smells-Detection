// getDTDContentModelHandler():  XMLDTDContentModelHandler  
//  
// XMLDTDContentModelHandler and XMLDTDHandler methods  
//  
/**
     * The start of the DTD external subset.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
    if (fDTDGrammar != null)
        fDTDGrammar.startExternalSubset(identifier, augs);
    if (fDTDHandler != null) {
        fDTDHandler.startExternalSubset(identifier, augs);
    }
}
