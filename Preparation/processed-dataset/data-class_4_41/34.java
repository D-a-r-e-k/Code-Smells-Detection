// setScannerState(short) 
// scanning 
/** Scans a DOCTYPE line. */
protected void scanDoctype() throws IOException {
    String root = null;
    String pubid = null;
    String sysid = null;
    if (skipSpaces()) {
        root = scanName();
        if (root == null) {
            if (fReportErrors) {
                fErrorReporter.reportError("HTML1014", null);
            }
        } else {
            root = modifyName(root, fNamesElems);
        }
        if (skipSpaces()) {
            if (skip("PUBLIC", false)) {
                skipSpaces();
                pubid = scanLiteral();
                if (skipSpaces()) {
                    sysid = scanLiteral();
                }
            } else if (skip("SYSTEM", false)) {
                skipSpaces();
                sysid = scanLiteral();
            }
        }
    }
    int c;
    while ((c = fCurrentEntity.read()) != -1) {
        if (c == '<') {
            fCurrentEntity.rewind();
            break;
        }
        if (c == '>') {
            break;
        }
        if (c == '[') {
            skipMarkup(true);
            break;
        }
    }
    if (fDocumentHandler != null) {
        if (fOverrideDoctype) {
            pubid = fDoctypePubid;
            sysid = fDoctypeSysid;
        }
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.doctypeDecl(root, pubid, sysid, locationAugs());
    }
}
