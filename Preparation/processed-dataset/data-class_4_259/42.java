protected PdfDictionary readXrefSection() throws IOException {
    tokens.nextValidToken();
    if (!tokens.getStringValue().equals("xref"))
        tokens.throwError(MessageLocalization.getComposedMessage("xref.subsection.not.found"));
    int start = 0;
    int end = 0;
    int pos = 0;
    int gen = 0;
    while (true) {
        tokens.nextValidToken();
        if (tokens.getStringValue().equals("trailer"))
            break;
        if (tokens.getTokenType() != TokenType.NUMBER)
            tokens.throwError(MessageLocalization.getComposedMessage("object.number.of.the.first.object.in.this.xref.subsection.not.found"));
        start = tokens.intValue();
        tokens.nextValidToken();
        if (tokens.getTokenType() != TokenType.NUMBER)
            tokens.throwError(MessageLocalization.getComposedMessage("number.of.entries.in.this.xref.subsection.not.found"));
        end = tokens.intValue() + start;
        if (start == 1) {
            // fix incorrect start number 
            int back = tokens.getFilePointer();
            tokens.nextValidToken();
            pos = tokens.intValue();
            tokens.nextValidToken();
            gen = tokens.intValue();
            if (pos == 0 && gen == PdfWriter.GENERATION_MAX) {
                --start;
                --end;
            }
            tokens.seek(back);
        }
        ensureXrefSize(end * 2);
        for (int k = start; k < end; ++k) {
            tokens.nextValidToken();
            pos = tokens.intValue();
            tokens.nextValidToken();
            gen = tokens.intValue();
            tokens.nextValidToken();
            int p = k * 2;
            if (tokens.getStringValue().equals("n")) {
                if (xref[p] == 0 && xref[p + 1] == 0) {
                    //                        if (pos == 0) 
                    //                            tokens.throwError(MessageLocalization.getComposedMessage("file.position.0.cross.reference.entry.in.this.xref.subsection")); 
                    xref[p] = pos;
                }
            } else if (tokens.getStringValue().equals("f")) {
                if (xref[p] == 0 && xref[p + 1] == 0)
                    xref[p] = -1;
            } else
                tokens.throwError(MessageLocalization.getComposedMessage("invalid.cross.reference.entry.in.this.xref.subsection"));
        }
    }
    PdfDictionary trailer = (PdfDictionary) readPRObject();
    PdfNumber xrefSize = (PdfNumber) trailer.get(PdfName.SIZE);
    ensureXrefSize(xrefSize.intValue() * 2);
    PdfObject xrs = trailer.get(PdfName.XREFSTM);
    if (xrs != null && xrs.isNumber()) {
        int loc = ((PdfNumber) xrs).intValue();
        try {
            readXRefStream(loc);
            newXrefType = true;
            hybridXref = true;
        } catch (IOException e) {
            xref = null;
            throw e;
        }
    }
    return trailer;
}
