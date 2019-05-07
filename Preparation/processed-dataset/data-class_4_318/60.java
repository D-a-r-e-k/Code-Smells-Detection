// processRootElementDeclQName  
void checkElementMatchesRootElementDecl(final XSElementDecl rootElementDecl, final QName element) {
    // Report an error if the name of the element does   
    // not match the name of the specified element declaration.  
    if (element.localpart != rootElementDecl.fName || element.uri != rootElementDecl.fTargetNamespace) {
        reportSchemaError("cvc-elt.1.b", new Object[] { element.rawname, rootElementDecl.fName });
    }
}
