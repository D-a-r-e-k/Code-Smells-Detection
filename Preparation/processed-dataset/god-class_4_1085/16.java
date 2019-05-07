/** The information in the maps of the table 'cmap' is coded in several formats.
     *  Format 4 is the Microsoft standard character to glyph index mapping table.
     * @return a <CODE>HashMap</CODE> representing this map
     * @throws IOException the font file could not be read
     */
HashMap<Integer, int[]> readFormat4() throws IOException {
    HashMap<Integer, int[]> h = new HashMap<Integer, int[]>();
    int table_lenght = rf.readUnsignedShort();
    rf.skipBytes(2);
    int segCount = rf.readUnsignedShort() / 2;
    rf.skipBytes(6);
    int endCount[] = new int[segCount];
    for (int k = 0; k < segCount; ++k) {
        endCount[k] = rf.readUnsignedShort();
    }
    rf.skipBytes(2);
    int startCount[] = new int[segCount];
    for (int k = 0; k < segCount; ++k) {
        startCount[k] = rf.readUnsignedShort();
    }
    int idDelta[] = new int[segCount];
    for (int k = 0; k < segCount; ++k) {
        idDelta[k] = rf.readUnsignedShort();
    }
    int idRO[] = new int[segCount];
    for (int k = 0; k < segCount; ++k) {
        idRO[k] = rf.readUnsignedShort();
    }
    int glyphId[] = new int[table_lenght / 2 - 8 - segCount * 4];
    for (int k = 0; k < glyphId.length; ++k) {
        glyphId[k] = rf.readUnsignedShort();
    }
    for (int k = 0; k < segCount; ++k) {
        int glyph;
        for (int j = startCount[k]; j <= endCount[k] && j != 0xFFFF; ++j) {
            if (idRO[k] == 0) {
                glyph = j + idDelta[k] & 0xFFFF;
            } else {
                int idx = k + idRO[k] / 2 - segCount + j - startCount[k];
                if (idx >= glyphId.length)
                    continue;
                glyph = glyphId[idx] + idDelta[k] & 0xFFFF;
            }
            int r[] = new int[2];
            r[0] = glyph;
            r[1] = getGlyphWidth(r[0]);
            h.put(new Integer(fontSpecific ? ((j & 0xff00) == 0xf000 ? j & 0xff : j) : j), r);
        }
    }
    return h;
}
