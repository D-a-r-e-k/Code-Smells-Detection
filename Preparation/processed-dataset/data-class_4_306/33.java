// endConditional()  
/**
     * The end of the DTD.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endDTD(Augmentations augs) throws XNIException {
    // save grammar  
    if (fDTDGrammar != null) {
        fDTDGrammar.endDTD(augs);
        if (fGrammarPool != null)
            fGrammarPool.cacheGrammars(XMLGrammarDescription.XML_DTD, new Grammar[] { fDTDGrammar });
    }
    if (fValidation) {
        DTDGrammar grammar = (fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar());
        // VC : Notation Declared. for external entity declaration [Production 76].  
        Iterator entities = fNDataDeclNotations.entrySet().iterator();
        while (entities.hasNext()) {
            Map.Entry entry = (Map.Entry) entities.next();
            String notation = (String) entry.getValue();
            if (grammar.getNotationDeclIndex(notation) == -1) {
                String entity = (String) entry.getKey();
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_NOTATION_NOT_DECLARED_FOR_UNPARSED_ENTITYDECL", new Object[] { entity, notation }, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
        // VC: Notation Attributes:  
        //     all notation names in the (attribute) declaration must be declared.  
        Iterator notationVals = fNotationEnumVals.entrySet().iterator();
        while (notationVals.hasNext()) {
            Map.Entry entry = (Map.Entry) notationVals.next();
            String notation = (String) entry.getKey();
            if (grammar.getNotationDeclIndex(notation) == -1) {
                String attributeName = (String) entry.getValue();
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_NOTATION_NOT_DECLARED_FOR_NOTATIONTYPE_ATTRIBUTE", new Object[] { attributeName, notation }, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
        // VC: No Notation on Empty Element  
        // An attribute of type NOTATION must not be declared on an element declared EMPTY.  
        Iterator elementsWithNotations = fTableOfNOTATIONAttributeNames.entrySet().iterator();
        while (elementsWithNotations.hasNext()) {
            Map.Entry entry = (Map.Entry) elementsWithNotations.next();
            String elementName = (String) entry.getKey();
            int elementIndex = grammar.getElementDeclIndex(elementName);
            if (grammar.getContentSpecType(elementIndex) == XMLElementDecl.TYPE_EMPTY) {
                String attributeName = (String) entry.getValue();
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "NoNotationOnEmptyElement", new Object[] { elementName, attributeName }, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
        // should be safe to release these references  
        fTableOfIDAttributeNames = null;
        fTableOfNOTATIONAttributeNames = null;
        // check whether each element referenced in a content model is declared  
        if (fWarnOnUndeclaredElemdef) {
            checkDeclaredElements(grammar);
        }
    }
    // call handlers  
    if (fDTDHandler != null) {
        fDTDHandler.endDTD(augs);
    }
}
