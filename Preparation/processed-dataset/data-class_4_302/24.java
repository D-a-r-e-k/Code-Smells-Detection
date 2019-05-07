// setScannerVersion(short)  
/** Returns the entity scanner. */
public XMLEntityScanner getEntityScanner() {
    if (fEntityScanner == null) {
        // default to 1.0  
        if (fXML10EntityScanner == null) {
            fXML10EntityScanner = new XMLEntityScanner();
        }
        fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
        fEntityScanner = fXML10EntityScanner;
    }
    return fEntityScanner;
}
