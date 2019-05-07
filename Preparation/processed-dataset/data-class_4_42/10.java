// scanPI() 
/** 
         * Scans a start element. 
         *
         * @param empty Is used for a second return value to indicate whether
         *              the start element tag is empty (e.g. "/&gt;").
         */
protected String scanStartElement(boolean[] empty) throws IOException {
    String ename = scanName();
    int length = ename != null ? ename.length() : 0;
    int c = length > 0 ? ename.charAt(0) : -1;
    if (length == 0 || !((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
        if (fReportErrors) {
            fErrorReporter.reportError("HTML1009", null);
        }
        if (fDocumentHandler != null && fElementCount >= fElementDepth) {
            fStringBuffer.clear();
            fStringBuffer.append('<');
            if (length > 0) {
                fStringBuffer.append(ename);
            }
            fDocumentHandler.characters(fStringBuffer, null);
        }
        return null;
    }
    ename = modifyName(ename, fNamesElems);
    fAttributes.removeAllAttributes();
    int beginLineNumber = fBeginLineNumber;
    int beginColumnNumber = fBeginColumnNumber;
    int beginCharacterOffset = fBeginCharacterOffset;
    while (scanAttribute(fAttributes, empty)) {
    }
    fBeginLineNumber = beginLineNumber;
    fBeginColumnNumber = beginColumnNumber;
    fBeginCharacterOffset = beginCharacterOffset;
    if (fByteStream != null && fElementDepth == -1) {
        if (ename.equalsIgnoreCase("META")) {
            if (DEBUG_CHARSET) {
                System.out.println("+++ <META>");
            }
            String httpEquiv = getValue(fAttributes, "http-equiv");
            if (httpEquiv != null && httpEquiv.equalsIgnoreCase("content-type")) {
                if (DEBUG_CHARSET) {
                    System.out.println("+++ @content-type: \"" + httpEquiv + '"');
                }
                String content = getValue(fAttributes, "content");
                if (content != null) {
                    content = removeSpaces(content);
                    int index1 = content.toLowerCase().indexOf("charset=");
                    if (index1 != -1 && !fIgnoreSpecifiedCharset) {
                        final int index2 = content.indexOf(';', index1);
                        final String charset = index2 != -1 ? content.substring(index1 + 8, index2) : content.substring(index1 + 8);
                        changeEncoding(charset);
                    }
                }
            }
        } else if (ename.equalsIgnoreCase("BODY")) {
            fByteStream.clear();
            fByteStream = null;
        } else {
            HTMLElements.Element element = HTMLElements.getElement(ename);
            if (element.parent != null && element.parent.length > 0) {
                if (element.parent[0].code == HTMLElements.BODY) {
                    fByteStream.clear();
                    fByteStream = null;
                }
            }
        }
    }
    if (fDocumentHandler != null && fElementCount >= fElementDepth) {
        fQName.setValues(null, ename, ename, null);
        if (DEBUG_CALLBACKS) {
            System.out.println("startElement(" + fQName + ',' + fAttributes + ")");
        }
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        if (empty[0]) {
            fDocumentHandler.emptyElement(fQName, fAttributes, locationAugs());
        } else {
            fDocumentHandler.startElement(fQName, fAttributes, locationAugs());
        }
    }
    return ename;
}
