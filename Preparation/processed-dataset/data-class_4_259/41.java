protected void readXref() throws IOException {
    hybridXref = false;
    newXrefType = false;
    tokens.seek(tokens.getStartxref());
    tokens.nextToken();
    if (!tokens.getStringValue().equals("startxref"))
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("startxref.not.found"));
    tokens.nextToken();
    if (tokens.getTokenType() != TokenType.NUMBER)
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("startxref.is.not.followed.by.a.number"));
    int startxref = tokens.intValue();
    lastXref = startxref;
    eofPos = tokens.getFilePointer();
    try {
        if (readXRefStream(startxref)) {
            newXrefType = true;
            return;
        }
    } catch (Exception e) {
    }
    xref = null;
    tokens.seek(startxref);
    trailer = readXrefSection();
    PdfDictionary trailer2 = trailer;
    while (true) {
        PdfNumber prev = (PdfNumber) trailer2.get(PdfName.PREV);
        if (prev == null)
            break;
        tokens.seek(prev.intValue());
        trailer2 = readXrefSection();
    }
}
