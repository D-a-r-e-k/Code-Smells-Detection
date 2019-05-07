// createContentDispatcher():Dispatcher  
// scanning methods  
/**
     * Scans an XML or text declaration.
     * <p>
     * <pre>
     * [23] XMLDecl ::= '&lt;?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
     * [24] VersionInfo ::= S 'version' Eq (' VersionNum ' | " VersionNum ")
     * [80] EncodingDecl ::= S 'encoding' Eq ('"' EncName '"' |  "'" EncName "'" )
     * [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
     * [32] SDDecl ::= S 'standalone' Eq (("'" ('yes' | 'no') "'")
     *                 | ('"' ('yes' | 'no') '"'))
     *
     * [77] TextDecl ::= '&lt;?xml' VersionInfo? EncodingDecl S? '?>'
     * </pre>
     *
     * @param scanningTextDecl True if a text declaration is to
     *                         be scanned instead of an XML
     *                         declaration.
     */
protected void scanXMLDeclOrTextDecl(boolean scanningTextDecl) throws IOException, XNIException {
    // scan decl  
    super.scanXMLDeclOrTextDecl(scanningTextDecl, fStrings);
    fMarkupDepth--;
    // pseudo-attribute values  
    String version = fStrings[0];
    String encoding = fStrings[1];
    String standalone = fStrings[2];
    // set standalone  
    fStandalone = standalone != null && standalone.equals("yes");
    fEntityManager.setStandalone(fStandalone);
    // set version on reader  
    fEntityScanner.setXMLVersion(version);
    // call handler  
    if (fDocumentHandler != null) {
        if (scanningTextDecl) {
            fDocumentHandler.textDecl(version, encoding, null);
        } else {
            fDocumentHandler.xmlDecl(version, encoding, standalone, null);
        }
    }
    // set encoding on reader  
    if (encoding != null && !fEntityScanner.fCurrentEntity.isEncodingExternallySpecified()) {
        fEntityScanner.setEncoding(encoding);
    }
}
