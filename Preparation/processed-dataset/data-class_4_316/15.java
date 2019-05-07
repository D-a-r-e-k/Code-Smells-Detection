//writeURI  
//  
//  Private methods  
//  
private void prepareForSerialization(XMLSerializer ser, Node node) {
    ser.reset();
    ser.features = features;
    ser.fDOMErrorHandler = fErrorHandler;
    ser.fNamespaces = (features & NAMESPACES) != 0;
    ser.fNamespacePrefixes = (features & NSDECL) != 0;
    ser._format.setIndenting((features & PRETTY_PRINT) != 0);
    ser._format.setOmitComments((features & COMMENTS) == 0);
    ser._format.setOmitXMLDeclaration((features & XMLDECL) == 0);
    if ((features & WELLFORMED) != 0) {
        // REVISIT: this is inefficient implementation of well-formness. Instead, we should check  
        // well-formness as we serialize the tree  
        Node next, root;
        root = node;
        Method versionChanged;
        boolean verifyNames = true;
        Document document = (node.getNodeType() == Node.DOCUMENT_NODE) ? (Document) node : node.getOwnerDocument();
        try {
            versionChanged = document.getClass().getMethod("isXMLVersionChanged()", new Class[] {});
            if (versionChanged != null) {
                verifyNames = ((Boolean) versionChanged.invoke(document, (Object[]) null)).booleanValue();
            }
        } catch (Exception e) {
        }
        if (node.getFirstChild() != null) {
            while (node != null) {
                verify(node, verifyNames, false);
                // Move down to first child  
                next = node.getFirstChild();
                // No child nodes, so walk tree  
                while (next == null) {
                    // Move to sibling if possible.  
                    next = node.getNextSibling();
                    if (next == null) {
                        node = node.getParentNode();
                        if (root == node) {
                            next = null;
                            break;
                        }
                        next = node.getNextSibling();
                    }
                }
                node = next;
            }
        } else {
            verify(node, verifyNames, false);
        }
    }
}
