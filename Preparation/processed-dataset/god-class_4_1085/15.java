/** The information in the maps of the table 'cmap' is coded in several formats.
     *  Format 0 is the Apple standard character to glyph index mapping table.
     * @return a <CODE>HashMap</CODE> representing this map
     * @throws IOException the font file could not be read
     */
HashMap<Integer, int[]> readFormat0() throws IOException {
    HashMap<Integer, int[]> h = new HashMap<Integer, int[]>();
    rf.skipBytes(4);
    for (int k = 0; k < 256; ++k) {
        int r[] = new int[2];
        r[0] = rf.readUnsignedByte();
        r[1] = getGlyphWidth(r[0]);
        h.put(new Integer(k), r);
    }
    return h;
}
