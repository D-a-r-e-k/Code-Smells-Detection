// scanDoctype() 
/** Scans a quoted literal. */
protected String scanLiteral() throws IOException {
    int quote = fCurrentEntity.read();
    if (quote == '\'' || quote == '"') {
        StringBuffer str = new StringBuffer();
        int c;
        while ((c = fCurrentEntity.read()) != -1) {
            if (c == quote) {
                break;
            }
            if (c == '\r' || c == '\n') {
                fCurrentEntity.rewind();
                // NOTE: This collapses newlines to a single space. 
                //       [Q] Is this the right thing to do here? -Ac 
                skipNewlines();
                str.append(' ');
            } else if (c == '<') {
                fCurrentEntity.rewind();
                break;
            } else {
                str.append((char) c);
            }
        }
        if (c == -1) {
            if (fReportErrors) {
                fErrorReporter.reportError("HTML1007", null);
            }
            throw new EOFException();
        }
        return str.toString();
    } else {
        fCurrentEntity.rewind();
    }
    return null;
}
