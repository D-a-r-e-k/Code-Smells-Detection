// setInputSource(XMLInputSource) 
/** Scans the document. */
public boolean scanDocument(boolean complete) throws XNIException, IOException {
    do {
        if (!fScanner.scan(complete)) {
            return false;
        }
    } while (complete);
    return true;
}
