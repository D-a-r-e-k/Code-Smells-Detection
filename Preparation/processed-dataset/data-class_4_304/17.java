// scanPIData(String)  
/**
     * Scans a comment.
     * <p>
     * <pre>
     * [15] Comment ::= '&lt!--' ((Char - '-') | ('-' (Char - '-')))* '-->'
     * </pre>
     * <p>
     * <strong>Note:</strong> Called after scanning past '&lt;!--'
     */
protected void scanComment() throws IOException, XNIException {
    scanComment(fStringBuffer);
    fMarkupDepth--;
    // call handler  
    if (fDocumentHandler != null) {
        fDocumentHandler.comment(fStringBuffer, null);
    }
}
