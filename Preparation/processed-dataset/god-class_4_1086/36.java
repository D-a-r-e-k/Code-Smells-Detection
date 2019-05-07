protected void readDocObj() throws IOException {
    ArrayList<PRStream> streams = new ArrayList<PRStream>();
    xrefObj = new ArrayList<PdfObject>(xref.length / 2);
    xrefObj.addAll(Collections.<PdfObject>nCopies(xref.length / 2, null));
    for (int k = 2; k < xref.length; k += 2) {
        int pos = xref[k];
        if (pos <= 0 || xref[k + 1] > 0)
            continue;
        tokens.seek(pos);
        tokens.nextValidToken();
        if (tokens.getTokenType() != TokenType.NUMBER)
            tokens.throwError(MessageLocalization.getComposedMessage("invalid.object.number"));
        objNum = tokens.intValue();
        tokens.nextValidToken();
        if (tokens.getTokenType() != TokenType.NUMBER)
            tokens.throwError(MessageLocalization.getComposedMessage("invalid.generation.number"));
        objGen = tokens.intValue();
        tokens.nextValidToken();
        if (!tokens.getStringValue().equals("obj"))
            tokens.throwError(MessageLocalization.getComposedMessage("token.obj.expected"));
        PdfObject obj;
        try {
            obj = readPRObject();
            if (obj.isStream()) {
                streams.add((PRStream) obj);
            }
        } catch (Exception e) {
            obj = null;
        }
        xrefObj.set(k / 2, obj);
    }
    for (int k = 0; k < streams.size(); ++k) {
        checkPRStreamLength(streams.get(k));
    }
    readDecryptedDocObj();
    if (objStmMark != null) {
        for (Map.Entry<Integer, IntHashtable> entry : objStmMark.entrySet()) {
            int n = entry.getKey().intValue();
            IntHashtable h = entry.getValue();
            readObjStm((PRStream) xrefObj.get(n), h);
            xrefObj.set(n, null);
        }
        objStmMark = null;
    }
    xref = null;
}
