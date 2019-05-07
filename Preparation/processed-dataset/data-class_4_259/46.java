protected PdfArray readArray() throws IOException {
    PdfArray array = new PdfArray();
    while (true) {
        PdfObject obj = readPRObject();
        int type = obj.type();
        if (-type == TokenType.END_ARRAY.ordinal())
            break;
        if (-type == TokenType.END_DIC.ordinal())
            tokens.throwError(MessageLocalization.getComposedMessage("unexpected.gt.gt"));
        array.add(obj);
    }
    return array;
}
