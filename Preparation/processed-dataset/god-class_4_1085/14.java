HashMap<Integer, int[]> readFormat12() throws IOException {
    HashMap<Integer, int[]> h = new HashMap<Integer, int[]>();
    rf.skipBytes(2);
    int table_lenght = rf.readInt();
    rf.skipBytes(4);
    int nGroups = rf.readInt();
    for (int k = 0; k < nGroups; k++) {
        int startCharCode = rf.readInt();
        int endCharCode = rf.readInt();
        int startGlyphID = rf.readInt();
        for (int i = startCharCode; i <= endCharCode; i++) {
            int[] r = new int[2];
            r[0] = startGlyphID;
            r[1] = getGlyphWidth(r[0]);
            h.put(new Integer(i), r);
            startGlyphID++;
        }
    }
    return h;
}
