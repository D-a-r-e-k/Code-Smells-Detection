protected PdfObject readSingleObject(int k) throws IOException {
    strings.clear();
    int k2 = k * 2;
    int pos = xref[k2];
    if (pos < 0)
        return null;
    if (xref[k2 + 1] > 0)
        pos = objStmToOffset.get(xref[k2 + 1]);
    if (pos == 0)
        return null;
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
        for (int j = 0; j < strings.size(); ++j) {
            PdfString str = strings.get(j);
            str.decrypt(this);
        }
        if (obj.isStream()) {
            checkPRStreamLength((PRStream) obj);
        }
    } catch (Exception e) {
        obj = null;
    }
    if (xref[k2 + 1] > 0) {
        obj = readOneObjStm((PRStream) obj, xref[k2]);
    }
    xrefObj.set(k, obj);
    return obj;
}
