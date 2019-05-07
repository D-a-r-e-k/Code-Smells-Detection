protected void rebuildXref() throws IOException {
    hybridXref = false;
    newXrefType = false;
    tokens.seek(0);
    int xr[][] = new int[1024][];
    int top = 0;
    trailer = null;
    byte line[] = new byte[64];
    for (; ; ) {
        int pos = tokens.getFilePointer();
        if (!tokens.readLineSegment(line))
            break;
        if (line[0] == 't') {
            if (!PdfEncodings.convertToString(line, null).startsWith("trailer"))
                continue;
            tokens.seek(pos);
            tokens.nextToken();
            pos = tokens.getFilePointer();
            try {
                PdfDictionary dic = (PdfDictionary) readPRObject();
                if (dic.get(PdfName.ROOT) != null)
                    trailer = dic;
                else
                    tokens.seek(pos);
            } catch (Exception e) {
                tokens.seek(pos);
            }
        } else if (line[0] >= '0' && line[0] <= '9') {
            int obj[] = PRTokeniser.checkObjectStart(line);
            if (obj == null)
                continue;
            int num = obj[0];
            int gen = obj[1];
            if (num >= xr.length) {
                int newLength = num * 2;
                int xr2[][] = new int[newLength][];
                System.arraycopy(xr, 0, xr2, 0, top);
                xr = xr2;
            }
            if (num >= top)
                top = num + 1;
            if (xr[num] == null || gen >= xr[num][1]) {
                obj[0] = pos;
                xr[num] = obj;
            }
        }
    }
    if (trailer == null)
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("trailer.not.found"));
    xref = new int[top * 2];
    for (int k = 0; k < top; ++k) {
        int obj[] = xr[k];
        if (obj != null)
            xref[k * 2] = obj[0];
    }
}
