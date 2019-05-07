// handleStartDocument(XMLLocator,String)  
void handleEndDocument() {
    if (fIDCChecking) {
        fValueStoreCache.endDocument();
    }
}
