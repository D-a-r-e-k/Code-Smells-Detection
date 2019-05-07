// scanPseudoAttribute(XMLString):String  
/**
     * Scans a processing instruction.
     * <p>
     * <pre>
     * [16] PI ::= '&lt;?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'
     * [17] PITarget ::= Name - (('X' | 'x') ('M' | 'm') ('L' | 'l'))
     * </pre>
     * <strong>Note:</strong> This method uses fString, anything in it
     * at the time of calling is lost.
     */
protected void scanPI() throws IOException, XNIException {
    // target  
    fReportEntity = false;
    String target = null;
    if (fNamespaces) {
        target = fEntityScanner.scanNCName();
    } else {
        target = fEntityScanner.scanName();
    }
    if (target == null) {
        reportFatalError("PITargetRequired", null);
    }
    // scan data  
    scanPIData(target, fString);
    fReportEntity = true;
}
