// processingInstruction(String,XMLString)  
//  
// XMLDTDHandler methods  
//  
/**
     * The start of the DTD.
     *
     * @param locator  The document locator, or null if the document
     *                 location cannot be reported during the parsing of 
     *                 the document DTD. However, it is <em>strongly</em>
     *                 recommended that a locator be supplied that can 
     *                 at least report the base system identifier of the
     *                 DTD.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startDTD(XMLLocator locator, Augmentations augs) throws XNIException {
    // initialize state  
    fNDataDeclNotations.clear();
    fDTDElementDecls.clear();
    // the grammar bucket's DTDGrammar will now be the  
    // one we want, whether we're constructing it or not.  
    // if we're not constructing it, then we should not have a reference  
    // to it!  
    if (!fGrammarBucket.getActiveGrammar().isImmutable()) {
        fDTDGrammar = fGrammarBucket.getActiveGrammar();
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.startDTD(locator, augs);
    if (fDTDHandler != null) {
        fDTDHandler.startDTD(locator, augs);
    }
}
