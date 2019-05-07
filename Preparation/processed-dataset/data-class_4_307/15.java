// xmlDecl(String,String,String)  
/**
     * Notifies of the presence of the DOCTYPE line in the document.
     * 
     * @param rootElement The name of the root element.
     * @param publicId    The public identifier if an external DTD or null
     *                    if the external DTD is specified using SYSTEM.
     * @param systemId    The system identifier if an external DTD, null
     *                    otherwise.     
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
    // save root element state  
    fSeenDoctypeDecl = true;
    fRootElement.setValues(null, rootElement, rootElement, null);
    // find or create grammar:  
    String eid = null;
    try {
        eid = XMLEntityManager.expandSystemId(systemId, fDocLocation.getExpandedSystemId(), false);
    } catch (java.io.IOException e) {
    }
    XMLDTDDescription grammarDesc = new XMLDTDDescription(publicId, systemId, fDocLocation.getExpandedSystemId(), eid, rootElement);
    fDTDGrammar = fGrammarBucket.getGrammar(grammarDesc);
    if (fDTDGrammar == null) {
        // give grammar pool a chance...  
        //  
        // Do not bother checking the pool if no public or system identifier was provided.   
        // Since so many different DTDs have roots in common, using only a root name as the   
        // key may cause an unexpected grammar to be retrieved from the grammar pool. This scenario  
        // would occur when an ExternalSubsetResolver has been queried and the  
        // XMLInputSource returned contains an input stream but no external identifier.  
        // This can never happen when the instance document specified a DOCTYPE. -- mrglavas  
        if (fGrammarPool != null && (systemId != null || publicId != null)) {
            fDTDGrammar = (DTDGrammar) fGrammarPool.retrieveGrammar(grammarDesc);
        }
    }
    if (fDTDGrammar == null) {
        // we'll have to create it...  
        if (!fBalanceSyntaxTrees) {
            fDTDGrammar = new DTDGrammar(fSymbolTable, grammarDesc);
        } else {
            fDTDGrammar = new BalancedDTDGrammar(fSymbolTable, grammarDesc);
        }
    } else {
        // we've found a cached one;so let's make sure not to read  
        // any external subset!  
        fValidationManager.setCachedDTD(true);
    }
    fGrammarBucket.setActiveGrammar(fDTDGrammar);
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
    }
}
