// scanStartElement():boolean  
/**
     * Scans the name of an element in a start or empty tag. 
     * 
     * @see #scanStartElement()
     */
protected void scanStartElementName() throws IOException, XNIException {
    // name  
    if (fNamespaces) {
        fEntityScanner.scanQName(fElementQName);
    } else {
        String name = fEntityScanner.scanName();
        fElementQName.setValues(null, name, name, null);
    }
    // Must skip spaces here because the DTD scanner  
    // would consume them at the end of the external subset.  
    fSawSpace = fEntityScanner.skipSpaces();
}
