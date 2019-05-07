protected PdfObject readOneObjStm(PRStream stream, int idx) throws IOException {
    int first = stream.getAsNumber(PdfName.FIRST).intValue();
    byte b[] = getStreamBytes(stream, tokens.getFile());
    PRTokeniser saveTokens = tokens;
    tokens = new PRTokeniser(b);
    try {
        int address = 0;
        boolean ok = true;
        ++idx;
        for (int k = 0; k < idx; ++k) {
            ok = tokens.nextToken();
            if (!ok)
                break;
            if (tokens.getTokenType() != TokenType.NUMBER) {
                ok = false;
                break;
            }
            ok = tokens.nextToken();
            if (!ok)
                break;
            if (tokens.getTokenType() != TokenType.NUMBER) {
                ok = false;
                break;
            }
            address = tokens.intValue() + first;
        }
        if (!ok)
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("error.reading.objstm"));
        tokens.seek(address);
        tokens.nextToken();
        PdfObject obj;
        if (tokens.getTokenType() == PRTokeniser.TokenType.NUMBER) {
            obj = new PdfNumber(tokens.getStringValue());
        } else {
            tokens.seek(address);
            obj = readPRObject();
        }
        return obj;
    } finally {
        tokens = saveTokens;
    }
}
