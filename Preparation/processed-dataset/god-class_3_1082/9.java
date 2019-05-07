/**
     * Converts a sequence of lines representing one of the column bounds into
     * an internal format.
     * <p>
     * Each array element will contain a <CODE>float[4]</CODE> representing
     * the line x = ax + b.
     *
     * @param cLine the column array
     * @return the converted array
     */
protected ArrayList<float[]> convertColumn(float cLine[]) {
    if (cLine.length < 4)
        throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found"));
    ArrayList<float[]> cc = new ArrayList<float[]>();
    for (int k = 0; k < cLine.length - 2; k += 2) {
        float x1 = cLine[k];
        float y1 = cLine[k + 1];
        float x2 = cLine[k + 2];
        float y2 = cLine[k + 3];
        if (y1 == y2)
            continue;
        // x = ay + b 
        float a = (x1 - x2) / (y1 - y2);
        float b = x1 - a * y1;
        float r[] = new float[4];
        r[0] = Math.min(y1, y2);
        r[1] = Math.max(y1, y2);
        r[2] = a;
        r[3] = b;
        cc.add(r);
        maxY = Math.max(maxY, r[1]);
        minY = Math.min(minY, r[0]);
    }
    if (cc.isEmpty())
        throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found"));
    return cc;
}
