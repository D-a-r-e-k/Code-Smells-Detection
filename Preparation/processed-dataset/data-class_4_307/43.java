// ensureStackCapacity  
//  
// Protected methods  
//  
/** Handle element
     * @return true if validator is removed from the pipeline
     */
protected boolean handleStartElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    // VC: Root Element Type  
    // see if the root element's name matches the one in DoctypeDecl   
    if (!fSeenRootElement) {
        // REVISIT: Here are current assumptions about validation features  
        //          given that XMLSchema validator is in the pipeline  
        //  
        // http://xml.org/sax/features/validation = true  
        // http://apache.org/xml/features/validation/schema = true  
        //  
        // [1] XML instance document only has reference to a DTD   
        //  Outcome: report validation errors only against dtd.  
        //  
        // [2] XML instance document has only XML Schema grammars:  
        //  Outcome: report validation errors only against schemas (no errors produced from DTD validator)  
        //  
        // [3] XML instance document has DTD and XML schemas:  
        // [a] if schema language is not set outcome - validation errors reported against both grammars: DTD and schemas.  
        // [b] if schema language is set to XML Schema - do not report validation errors  
        //           
        // if dynamic validation is on  
        //            validate only against grammar we've found (depending on settings  
        //            for schema feature)  
        //   
        //   
        fPerformValidation = validate();
        fSeenRootElement = true;
        fValidationManager.setEntityState(fDTDGrammar);
        fValidationManager.setGrammarFound(fSeenDoctypeDecl);
        rootElementSpecified(element);
    }
    if (fDTDGrammar == null) {
        if (!fPerformValidation) {
            fCurrentElementIndex = -1;
            fCurrentContentSpecType = -1;
            fInElementContent = false;
        }
        if (fPerformValidation) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_GRAMMAR_NOT_FOUND", new Object[] { element.rawname }, XMLErrorReporter.SEVERITY_ERROR);
        }
        // modify pipeline  
        if (fDocumentSource != null) {
            fDocumentSource.setDocumentHandler(fDocumentHandler);
            if (fDocumentHandler != null)
                fDocumentHandler.setDocumentSource(fDocumentSource);
            return true;
        }
    } else {
        //  resolve the element  
        fCurrentElementIndex = fDTDGrammar.getElementDeclIndex(element);
        //changed here.. new function for getContentSpecType  
        fCurrentContentSpecType = fDTDGrammar.getContentSpecType(fCurrentElementIndex);
        if (fCurrentContentSpecType == -1 && fPerformValidation) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_ELEMENT_NOT_DECLARED", new Object[] { element.rawname }, XMLErrorReporter.SEVERITY_ERROR);
        }
        //  0. insert default attributes  
        //  1. normalize the attributes  
        //  2. validate the attrivute list.  
        // TO DO:   
        //changed here.. also pass element name,  
        addDTDDefaultAttrsAndValidate(element, fCurrentElementIndex, attributes);
    }
    // set element content state  
    fInElementContent = fCurrentContentSpecType == XMLElementDecl.TYPE_CHILDREN;
    // increment the element depth, add this element's   
    // QName to its enclosing element 's children list  
    fElementDepth++;
    if (fPerformValidation) {
        // push current length onto stack  
        if (fElementChildrenOffsetStack.length <= fElementDepth) {
            int newarray[] = new int[fElementChildrenOffsetStack.length * 2];
            System.arraycopy(fElementChildrenOffsetStack, 0, newarray, 0, fElementChildrenOffsetStack.length);
            fElementChildrenOffsetStack = newarray;
        }
        fElementChildrenOffsetStack[fElementDepth] = fElementChildrenLength;
        // add this element to children  
        if (fElementChildren.length <= fElementChildrenLength) {
            QName[] newarray = new QName[fElementChildrenLength * 2];
            System.arraycopy(fElementChildren, 0, newarray, 0, fElementChildren.length);
            fElementChildren = newarray;
        }
        QName qname = fElementChildren[fElementChildrenLength];
        if (qname == null) {
            for (int i = fElementChildrenLength; i < fElementChildren.length; i++) {
                fElementChildren[i] = new QName();
            }
            qname = fElementChildren[fElementChildrenLength];
        }
        qname.setValues(element);
        fElementChildrenLength++;
    }
    // save current element information  
    fCurrentElement.setValues(element);
    ensureStackCapacity(fElementDepth);
    fElementQNamePartsStack[fElementDepth].setValues(fCurrentElement);
    fElementIndexStack[fElementDepth] = fCurrentElementIndex;
    fContentSpecTypeStack[fElementDepth] = fCurrentContentSpecType;
    startNamespaceScope(element, attributes, augs);
    return false;
}
