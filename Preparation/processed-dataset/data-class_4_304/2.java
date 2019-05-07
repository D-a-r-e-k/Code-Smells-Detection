// setInputSource(XMLInputSource)  
/** 
     * Scans a document.
     *
     * @param complete True if the scanner should scan the document
     *                 completely, pushing all events to the registered
     *                 document handler. A value of false indicates that
     *                 that the scanner should only scan the next portion
     *                 of the document and return. A scanner instance is
     *                 permitted to completely scan a document if it does
     *                 not support this "pull" scanning model.
     *
     * @return True if there is more to scan, false otherwise.
     */
public boolean scanDocument(boolean complete) throws IOException, XNIException {
    // reset entity scanner  
    fEntityScanner = fEntityManager.getEntityScanner();
    // keep dispatching "events"  
    fEntityManager.setEntityHandler(this);
    do {
        if (!fDispatcher.dispatch(complete)) {
            return false;
        }
    } while (complete);
    // return success  
    return true;
}
