protected PdfObject readPRObject() throws IOException {
    tokens.nextValidToken();
    TokenType type = tokens.getTokenType();
    switch(type) {
        case START_DIC:
            {
                ++readDepth;
                PdfDictionary dic = readDictionary();
                --readDepth;
                int pos = tokens.getFilePointer();
                // be careful in the trailer. May not be a "next" token. 
                boolean hasNext;
                do {
                    hasNext = tokens.nextToken();
                } while (hasNext && tokens.getTokenType() == TokenType.COMMENT);
                if (hasNext && tokens.getStringValue().equals("stream")) {
                    //skip whitespaces 
                    int ch;
                    do {
                        ch = tokens.read();
                    } while (ch == 32 || ch == 9 || ch == 0 || ch == 12);
                    if (ch != '\n')
                        ch = tokens.read();
                    if (ch != '\n')
                        tokens.backOnePosition(ch);
                    PRStream stream = new PRStream(this, tokens.getFilePointer());
                    stream.putAll(dic);
                    // crypto handling 
                    stream.setObjNum(objNum, objGen);
                    return stream;
                } else {
                    tokens.seek(pos);
                    return dic;
                }
            }
        case START_ARRAY:
            {
                ++readDepth;
                PdfArray arr = readArray();
                --readDepth;
                return arr;
            }
        case NUMBER:
            return new PdfNumber(tokens.getStringValue());
        case STRING:
            PdfString str = new PdfString(tokens.getStringValue(), null).setHexWriting(tokens.isHexString());
            // crypto handling 
            str.setObjNum(objNum, objGen);
            if (strings != null)
                strings.add(str);
            return str;
        case NAME:
            {
                PdfName cachedName = PdfName.staticNames.get(tokens.getStringValue());
                if (readDepth > 0 && cachedName != null) {
                    return cachedName;
                } else {
                    // an indirect name (how odd...), or a non-standard one 
                    return new PdfName(tokens.getStringValue(), false);
                }
            }
        case REF:
            int num = tokens.getReference();
            PRIndirectReference ref = new PRIndirectReference(this, num, tokens.getGeneration());
            return ref;
        case ENDOFFILE:
            throw new IOException(MessageLocalization.getComposedMessage("unexpected.end.of.file"));
        default:
            String sv = tokens.getStringValue();
            if ("null".equals(sv)) {
                if (readDepth == 0) {
                    return new PdfNull();
                }
                //else 
                return PdfNull.PDFNULL;
            } else if ("true".equals(sv)) {
                if (readDepth == 0) {
                    return new PdfBoolean(true);
                }
                //else 
                return PdfBoolean.PDFTRUE;
            } else if ("false".equals(sv)) {
                if (readDepth == 0) {
                    return new PdfBoolean(false);
                }
                //else 
                return PdfBoolean.PDFFALSE;
            }
            return new PdfLiteral(-type.ordinal(), tokens.getStringValue());
    }
}
