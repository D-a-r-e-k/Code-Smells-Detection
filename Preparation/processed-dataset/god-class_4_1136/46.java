// handleEndElement(QName,boolean)  
protected void endNamespaceScope(QName element, Augmentations augs, boolean isEmpty) {
    // call handlers  
    if (fDocumentHandler != null && !isEmpty) {
        // NOTE: The binding of the element doesn't actually happen  
        //       yet because the namespace binder does that. However,  
        //       if it does it before this point, then the endPrefix-  
        //       Mapping calls get made too soon! As long as the  
        //       rawnames match, we know it'll have a good binding,  
        //       so we can just use the current element. -Ac  
        fDocumentHandler.endElement(fCurrentElement, augs);
    }
}
