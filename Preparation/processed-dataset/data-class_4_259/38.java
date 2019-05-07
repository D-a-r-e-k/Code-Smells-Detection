protected void readObjStm(PRStream stream, IntHashtable map) throws IOException {
    int first = stream.getAsNumber(PdfName.FIRST).intValue();
    int n = stream.getAsNumber(PdfName.N).intValue();
    byte b[] = getStreamBytes(stream, tokens.getFile());
    PRTokeniser saveTokens = tokens;
    tokens = new PRTokeniser(b);
    try {
        int address[] = new int[n];
        int objNumber[] = new int[n];
        boolean ok = true;
        for (int k = 0; k < n; ++k) {
            ok = tokens.nextToken();
            if (!ok)
                break;
            if (tokens.getTokenType() != TokenType.NUMBER) {
                ok = false;
                break;
            }
            objNumber[k] = tokens.intValue();
            ok = tokens.nextToken();
            if (!ok)
                break;
            if (tokens.getTokenType() != TokenType.NUMBER) {
                ok = false;
                break;
            }
            address[k] = tokens.intValue() + first;
        }
        if (!ok)
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("error.reading.objstm"));
        for (int k = 0; k < n; ++k) {
            if (map.containsKey(k)) {
                tokens.seek(address[k]);
                tokens.nextToken();
                PdfObject obj;
                if (tokens.getTokenType() == PRTokeniser.TokenType.NUMBER) {
                    obj = new PdfNumber(tokens.getStringValue());
                } else {
                    tokens.seek(address[k]);
                    obj = readPRObject();
                }
                xrefObj.set(objNumber[k], obj);
            }
        }
    } finally {
        tokens = saveTokens;
    }
}
