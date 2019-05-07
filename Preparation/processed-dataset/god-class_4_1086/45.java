protected PdfDictionary readDictionary() throws IOException {
    PdfDictionary dic = new PdfDictionary();
    while (true) {
        tokens.nextValidToken();
        if (tokens.getTokenType() == TokenType.END_DIC)
            break;
        if (tokens.getTokenType() != TokenType.NAME)
            tokens.throwError(MessageLocalization.getComposedMessage("dictionary.key.is.not.a.name"));
        PdfName name = new PdfName(tokens.getStringValue(), false);
        PdfObject obj = readPRObject();
        int type = obj.type();
        if (-type == TokenType.END_DIC.ordinal())
            tokens.throwError(MessageLocalization.getComposedMessage("unexpected.gt.gt"));
        if (-type == TokenType.END_ARRAY.ordinal())
            tokens.throwError(MessageLocalization.getComposedMessage("unexpected.close.bracket"));
        dic.put(name, obj);
    }
    return dic;
}
