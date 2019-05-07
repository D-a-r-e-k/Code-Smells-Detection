// scanCharReference()  
/**
     * Scans an entity reference.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws XNIException Thrown if handler throws exception upon
     *                      notification.
     */
protected void scanEntityReference() throws IOException, XNIException {
    // name  
    String name = fEntityScanner.scanName();
    if (name == null) {
        reportFatalError("NameRequiredInReference", null);
        return;
    }
    // end  
    if (!fEntityScanner.skipChar(';')) {
        reportFatalError("SemicolonRequiredInReference", new Object[] { name });
    }
    fMarkupDepth--;
    // handle built-in entities  
    if (name == fAmpSymbol) {
        handleCharacter('&', fAmpSymbol);
    } else if (name == fLtSymbol) {
        handleCharacter('<', fLtSymbol);
    } else if (name == fGtSymbol) {
        handleCharacter('>', fGtSymbol);
    } else if (name == fQuotSymbol) {
        handleCharacter('"', fQuotSymbol);
    } else if (name == fAposSymbol) {
        handleCharacter('\'', fAposSymbol);
    } else if (fEntityManager.isUnparsedEntity(name)) {
        reportFatalError("ReferenceToUnparsedEntity", new Object[] { name });
    } else {
        if (!fEntityManager.isDeclaredEntity(name)) {
            if (fIsEntityDeclaredVC) {
                if (fValidation)
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "EntityNotDeclared", new Object[] { name }, XMLErrorReporter.SEVERITY_ERROR);
            } else {
                reportFatalError("EntityNotDeclared", new Object[] { name });
            }
        }
        fEntityManager.startEntity(name, false);
    }
}
