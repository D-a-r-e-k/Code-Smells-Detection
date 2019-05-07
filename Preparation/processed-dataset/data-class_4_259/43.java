protected boolean readXRefStream(int ptr) throws IOException {
    tokens.seek(ptr);
    int thisStream = 0;
    if (!tokens.nextToken())
        return false;
    if (tokens.getTokenType() != TokenType.NUMBER)
        return false;
    thisStream = tokens.intValue();
    if (!tokens.nextToken() || tokens.getTokenType() != TokenType.NUMBER)
        return false;
    if (!tokens.nextToken() || !tokens.getStringValue().equals("obj"))
        return false;
    PdfObject object = readPRObject();
    PRStream stm = null;
    if (object.isStream()) {
        stm = (PRStream) object;
        if (!PdfName.XREF.equals(stm.get(PdfName.TYPE)))
            return false;
    } else
        return false;
    if (trailer == null) {
        trailer = new PdfDictionary();
        trailer.putAll(stm);
    }
    stm.setLength(((PdfNumber) stm.get(PdfName.LENGTH)).intValue());
    int size = ((PdfNumber) stm.get(PdfName.SIZE)).intValue();
    PdfArray index;
    PdfObject obj = stm.get(PdfName.INDEX);
    if (obj == null) {
        index = new PdfArray();
        index.add(new int[] { 0, size });
    } else
        index = (PdfArray) obj;
    PdfArray w = (PdfArray) stm.get(PdfName.W);
    int prev = -1;
    obj = stm.get(PdfName.PREV);
    if (obj != null)
        prev = ((PdfNumber) obj).intValue();
    // Each xref pair is a position 
    // type 0 -> -1, 0 
    // type 1 -> offset, 0 
    // type 2 -> index, obj num 
    ensureXrefSize(size * 2);
    if (objStmMark == null && !partial)
        objStmMark = new HashMap<Integer, IntHashtable>();
    if (objStmToOffset == null && partial)
        objStmToOffset = new IntHashtable();
    byte b[] = getStreamBytes(stm, tokens.getFile());
    int bptr = 0;
    int wc[] = new int[3];
    for (int k = 0; k < 3; ++k) wc[k] = w.getAsNumber(k).intValue();
    for (int idx = 0; idx < index.size(); idx += 2) {
        int start = index.getAsNumber(idx).intValue();
        int length = index.getAsNumber(idx + 1).intValue();
        ensureXrefSize((start + length) * 2);
        while (length-- > 0) {
            int type = 1;
            if (wc[0] > 0) {
                type = 0;
                for (int k = 0; k < wc[0]; ++k) type = (type << 8) + (b[bptr++] & 0xff);
            }
            int field2 = 0;
            for (int k = 0; k < wc[1]; ++k) field2 = (field2 << 8) + (b[bptr++] & 0xff);
            int field3 = 0;
            for (int k = 0; k < wc[2]; ++k) field3 = (field3 << 8) + (b[bptr++] & 0xff);
            int base = start * 2;
            if (xref[base] == 0 && xref[base + 1] == 0) {
                switch(type) {
                    case 0:
                        xref[base] = -1;
                        break;
                    case 1:
                        xref[base] = field2;
                        break;
                    case 2:
                        xref[base] = field3;
                        xref[base + 1] = field2;
                        if (partial) {
                            objStmToOffset.put(field2, 0);
                        } else {
                            Integer on = new Integer(field2);
                            IntHashtable seq = objStmMark.get(on);
                            if (seq == null) {
                                seq = new IntHashtable();
                                seq.put(field3, 1);
                                objStmMark.put(on, seq);
                            } else
                                seq.put(field3, 1);
                        }
                        break;
                }
            }
            ++start;
        }
    }
    thisStream *= 2;
    if (thisStream < xref.length)
        xref[thisStream] = -1;
    if (prev == -1)
        return true;
    return readXRefStream(prev);
}
