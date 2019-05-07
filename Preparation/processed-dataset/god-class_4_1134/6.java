// common scanning methods  
/**
     * Scans an XML or text declaration.
     * <p>
     * <pre>
     * [23] XMLDecl ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
     * [24] VersionInfo ::= S 'version' Eq (' VersionNum ' | " VersionNum ")
     * [80] EncodingDecl ::= S 'encoding' Eq ('"' EncName '"' |  "'" EncName "'" )
     * [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
     * [32] SDDecl ::= S 'standalone' Eq (("'" ('yes' | 'no') "'")
     *                 | ('"' ('yes' | 'no') '"'))
     *
     * [77] TextDecl ::= '<?xml' VersionInfo? EncodingDecl S? '?>'
     * </pre>
     *
     * @param scanningTextDecl True if a text declaration is to
     *                         be scanned instead of an XML
     *                         declaration.
     * @param pseudoAttributeValues An array of size 3 to return the version,
     *                         encoding and standalone pseudo attribute values
     *                         (in that order).
     *
     * <strong>Note:</strong> This method uses fString, anything in it
     * at the time of calling is lost.
     */
protected void scanXMLDeclOrTextDecl(boolean scanningTextDecl, String[] pseudoAttributeValues) throws IOException, XNIException {
    // pseudo-attribute values  
    String version = null;
    String encoding = null;
    String standalone = null;
    // scan pseudo-attributes  
    final int STATE_VERSION = 0;
    final int STATE_ENCODING = 1;
    final int STATE_STANDALONE = 2;
    final int STATE_DONE = 3;
    int state = STATE_VERSION;
    boolean dataFoundForTarget = false;
    boolean sawSpace = fEntityScanner.skipDeclSpaces();
    // since pseudoattributes are *not* attributes,  
    // their quotes don't need to be preserved in external parameter entities.  
    // the XMLEntityScanner#scanLiteral method will continue to  
    // emit -1 in such cases when it finds a quote; this is  
    // fine for other methods that parse scanned entities,  
    // but not for the scanning of pseudoattributes.  So,  
    // temporarily, we must mark the current entity as not being "literal"  
    XMLEntityManager.ScannedEntity currEnt = fEntityManager.getCurrentEntity();
    boolean currLiteral = currEnt.literal;
    currEnt.literal = false;
    while (fEntityScanner.peekChar() != '?') {
        dataFoundForTarget = true;
        String name = scanPseudoAttribute(scanningTextDecl, fString);
        switch(state) {
            case STATE_VERSION:
                {
                    if (name == fVersionSymbol) {
                        if (!sawSpace) {
                            reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeVersionInTextDecl" : "SpaceRequiredBeforeVersionInXMLDecl", null);
                        }
                        version = fString.toString();
                        state = STATE_ENCODING;
                        if (!versionSupported(version)) {
                            reportFatalError(getVersionNotSupportedKey(), new Object[] { version });
                        }
                    } else if (name == fEncodingSymbol) {
                        if (!scanningTextDecl) {
                            reportFatalError("VersionInfoRequired", null);
                        }
                        if (!sawSpace) {
                            reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
                        }
                        encoding = fString.toString();
                        state = scanningTextDecl ? STATE_DONE : STATE_STANDALONE;
                    } else {
                        if (scanningTextDecl) {
                            reportFatalError("EncodingDeclRequired", null);
                        } else {
                            reportFatalError("VersionInfoRequired", null);
                        }
                    }
                    break;
                }
            case STATE_ENCODING:
                {
                    if (name == fEncodingSymbol) {
                        if (!sawSpace) {
                            reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
                        }
                        encoding = fString.toString();
                        state = scanningTextDecl ? STATE_DONE : STATE_STANDALONE;
                    } else if (!scanningTextDecl && name == fStandaloneSymbol) {
                        if (!sawSpace) {
                            reportFatalError("SpaceRequiredBeforeStandalone", null);
                        }
                        standalone = fString.toString();
                        state = STATE_DONE;
                        if (!standalone.equals("yes") && !standalone.equals("no")) {
                            reportFatalError("SDDeclInvalid", new Object[] { standalone });
                        }
                    } else {
                        reportFatalError("EncodingDeclRequired", null);
                    }
                    break;
                }
            case STATE_STANDALONE:
                {
                    if (name == fStandaloneSymbol) {
                        if (!sawSpace) {
                            reportFatalError("SpaceRequiredBeforeStandalone", null);
                        }
                        standalone = fString.toString();
                        state = STATE_DONE;
                        if (!standalone.equals("yes") && !standalone.equals("no")) {
                            reportFatalError("SDDeclInvalid", new Object[] { standalone });
                        }
                    } else {
                        reportFatalError("EncodingDeclRequired", null);
                    }
                    break;
                }
            default:
                {
                    reportFatalError("NoMorePseudoAttributes", null);
                }
        }
        sawSpace = fEntityScanner.skipDeclSpaces();
    }
    // restore original literal value  
    if (currLiteral)
        currEnt.literal = true;
    // REVISIT: should we remove this error reporting?  
    if (scanningTextDecl && state != STATE_DONE) {
        reportFatalError("MorePseudoAttributes", null);
    }
    // If there is no data in the xml or text decl then we fail to report error   
    // for version or encoding info above.  
    if (scanningTextDecl) {
        if (!dataFoundForTarget && encoding == null) {
            reportFatalError("EncodingDeclRequired", null);
        }
    } else {
        if (!dataFoundForTarget && version == null) {
            reportFatalError("VersionInfoRequired", null);
        }
    }
    // end  
    if (!fEntityScanner.skipChar('?')) {
        reportFatalError("XMLDeclUnterminated", null);
    }
    if (!fEntityScanner.skipChar('>')) {
        reportFatalError("XMLDeclUnterminated", null);
    }
    // fill in return array  
    pseudoAttributeValues[0] = version;
    pseudoAttributeValues[1] = encoding;
    pseudoAttributeValues[2] = standalone;
}
