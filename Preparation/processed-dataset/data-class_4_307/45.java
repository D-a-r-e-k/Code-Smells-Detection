/** Handle end element. */
protected void handleEndElement(QName element, Augmentations augs, boolean isEmpty) throws XNIException {
    // decrease element depth  
    fElementDepth--;
    // validate  
    if (fPerformValidation) {
        int elementIndex = fCurrentElementIndex;
        if (elementIndex != -1 && fCurrentContentSpecType != -1) {
            QName children[] = fElementChildren;
            int childrenOffset = fElementChildrenOffsetStack[fElementDepth + 1] + 1;
            int childrenLength = fElementChildrenLength - childrenOffset;
            int result = checkContent(elementIndex, children, childrenOffset, childrenLength);
            if (result != -1) {
                fDTDGrammar.getElementDecl(elementIndex, fTempElementDecl);
                if (fTempElementDecl.type == XMLElementDecl.TYPE_EMPTY) {
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_CONTENT_INVALID", new Object[] { element.rawname, "EMPTY" }, XMLErrorReporter.SEVERITY_ERROR);
                } else {
                    String messageKey = result != childrenLength ? "MSG_CONTENT_INVALID" : "MSG_CONTENT_INCOMPLETE";
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, messageKey, new Object[] { element.rawname, fDTDGrammar.getContentSpecAsString(elementIndex) }, XMLErrorReporter.SEVERITY_ERROR);
                }
            }
        }
        fElementChildrenLength = fElementChildrenOffsetStack[fElementDepth + 1] + 1;
    }
    endNamespaceScope(fCurrentElement, augs, isEmpty);
    // now pop this element off the top of the element stack  
    if (fElementDepth < -1) {
        throw new RuntimeException("FWK008 Element stack underflow");
    }
    if (fElementDepth < 0) {
        fCurrentElement.clear();
        fCurrentElementIndex = -1;
        fCurrentContentSpecType = -1;
        fInElementContent = false;
        // TO DO : fix this  
        //  
        // Check after document is fully parsed  
        // (1) check that there was an element with a matching id for every  
        //   IDREF and IDREFS attr (V_IDREF0)  
        //  
        if (fPerformValidation) {
            String value = fValidationState.checkIDRefID();
            if (value != null) {
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_ELEMENT_WITH_ID_REQUIRED", new Object[] { value }, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
        return;
    }
    // If Namespace enable then localName != rawName  
    fCurrentElement.setValues(fElementQNamePartsStack[fElementDepth]);
    fCurrentElementIndex = fElementIndexStack[fElementDepth];
    fCurrentContentSpecType = fContentSpecTypeStack[fElementDepth];
    fInElementContent = (fCurrentContentSpecType == XMLElementDecl.TYPE_CHILDREN);
}
