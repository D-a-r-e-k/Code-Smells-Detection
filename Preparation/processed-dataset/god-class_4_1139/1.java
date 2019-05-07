//  
// Node methods  
//  
/**
     * Deep-clone a document, including fixing ownerDoc for the cloned
     * children. Note that this requires bypassing the WRONG_DOCUMENT_ERR
     * protection. I've chosen to implement it by calling importNode
     * which is DOM Level 2.
     *
     * @return org.w3c.dom.Node
     * @param deep boolean, iff true replicate children
     */
public Node cloneNode(boolean deep) {
    DocumentImpl newdoc = new DocumentImpl();
    callUserDataHandlers(this, newdoc, UserDataHandler.NODE_CLONED);
    cloneNode(newdoc, deep);
    // experimental  
    newdoc.mutationEvents = mutationEvents;
    return newdoc;
}
