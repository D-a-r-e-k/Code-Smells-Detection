// ensureStackCapacity  
// handle start document  
void handleStartDocument(XMLLocator locator, String encoding) {
    if (fIDCChecking) {
        fValueStoreCache.startDocument();
    }
    if (fAugPSVI) {
        fCurrentPSVI.fGrammars = null;
        fCurrentPSVI.fSchemaInformation = null;
    }
}
