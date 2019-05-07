// addLocationItem(XMLAttributes,int) 
/** Scans an end element. */
protected void scanEndElement() throws IOException {
    String ename = scanName();
    if (fReportErrors && ename == null) {
        fErrorReporter.reportError("HTML1012", null);
    }
    skipMarkup(false);
    if (ename != null) {
        ename = modifyName(ename, fNamesElems);
        if (fDocumentHandler != null && fElementCount >= fElementDepth) {
            fQName.setValues(null, ename, ename, null);
            if (DEBUG_CALLBACKS) {
                System.out.println("endElement(" + fQName + ")");
            }
            fEndLineNumber = fCurrentEntity.getLineNumber();
            fEndColumnNumber = fCurrentEntity.getColumnNumber();
            fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
            fDocumentHandler.endElement(fQName, locationAugs());
        }
    }
}
