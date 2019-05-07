// scanComment() 
/** Scans markup content. */
protected boolean scanMarkupContent(XMLStringBuffer buffer, char cend) throws IOException {
    int c = -1;
    OUTER: while (true) {
        c = fCurrentEntity.read();
        if (c == cend) {
            int count = 1;
            while (true) {
                c = fCurrentEntity.read();
                if (c == cend) {
                    count++;
                    continue;
                }
                break;
            }
            if (c == -1) {
                if (fReportErrors) {
                    fErrorReporter.reportError("HTML1007", null);
                }
                break OUTER;
            }
            if (count < 2) {
                buffer.append(cend);
                //if (c != -1) { 
                fCurrentEntity.rewind();
                //} 
                continue;
            }
            if (c != '>') {
                for (int i = 0; i < count; i++) {
                    buffer.append(cend);
                }
                fCurrentEntity.rewind();
                continue;
            }
            for (int i = 0; i < count - 2; i++) {
                buffer.append(cend);
            }
            break;
        } else if (c == '\n' || c == '\r') {
            fCurrentEntity.rewind();
            int newlines = skipNewlines();
            for (int i = 0; i < newlines; i++) {
                buffer.append('\n');
            }
            continue;
        } else if (c == -1) {
            if (fReportErrors) {
                fErrorReporter.reportError("HTML1007", null);
            }
            break;
        }
        buffer.append((char) c);
    }
    return c == -1;
}
