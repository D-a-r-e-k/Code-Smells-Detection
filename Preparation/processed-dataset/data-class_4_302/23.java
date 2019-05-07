//setupCurrentEntity(String, XMLInputSource, boolean, boolean):  String  
// set version of scanner to use  
public void setScannerVersion(short version) {
    if (version == Constants.XML_VERSION_1_0) {
        if (fXML10EntityScanner == null) {
            fXML10EntityScanner = new XMLEntityScanner();
        }
        fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
        fEntityScanner = fXML10EntityScanner;
        fEntityScanner.setCurrentEntity(fCurrentEntity);
    } else {
        if (fXML11EntityScanner == null) {
            fXML11EntityScanner = new XML11EntityScanner();
        }
        fXML11EntityScanner.reset(fSymbolTable, this, fErrorReporter);
        fEntityScanner = fXML11EntityScanner;
        fEntityScanner.setCurrentEntity(fCurrentEntity);
    }
}
