// scanStartElementAfterName()  
/** 
     * Scans an attribute.
     * <p>
     * <pre>
     * [41] Attribute ::= Name Eq AttValue
     * </pre> 
     * <p>
     * <strong>Note:</strong> This method assumes that the next 
     * character on the stream is the first character of the attribute
     * name.
     * <p>
     * <strong>Note:</strong> This method uses the fAttributeQName and
     * fQName variables. The contents of these variables will be
     * destroyed.
     *
     * @param attributes The attributes list for the scanned attribute.
     */
protected void scanAttribute(XMLAttributes attributes) throws IOException, XNIException {
    if (DEBUG_CONTENT_SCANNING)
        System.out.println(">>> scanAttribute()");
    // name  
    if (fNamespaces) {
        fEntityScanner.scanQName(fAttributeQName);
    } else {
        String name = fEntityScanner.scanName();
        fAttributeQName.setValues(null, name, name, null);
    }
    // equals  
    fEntityScanner.skipSpaces();
    if (!fEntityScanner.skipChar('=')) {
        reportFatalError("EqRequiredInAttribute", new Object[] { fCurrentElement.rawname, fAttributeQName.rawname });
    }
    fEntityScanner.skipSpaces();
    // content  
    int oldLen = attributes.getLength();
    int attrIndex = attributes.addAttribute(fAttributeQName, XMLSymbols.fCDATASymbol, null);
    // WFC: Unique Att Spec  
    if (oldLen == attributes.getLength()) {
        reportFatalError("AttributeNotUnique", new Object[] { fCurrentElement.rawname, fAttributeQName.rawname });
    }
    // Scan attribute value and return true if the un-normalized and normalized value are the same  
    boolean isSameNormalizedAttr = scanAttributeValue(fTempString, fTempString2, fAttributeQName.rawname, fIsEntityDeclaredVC, fCurrentElement.rawname);
    attributes.setValue(attrIndex, fTempString.toString());
    // If the non-normalized and normalized value are the same, avoid creating a new string.  
    if (!isSameNormalizedAttr) {
        attributes.setNonNormalizedValue(attrIndex, fTempString2.toString());
    }
    attributes.setSpecified(attrIndex, true);
    if (DEBUG_CONTENT_SCANNING)
        System.out.println("<<< scanAttribute()");
}
