/** The information in the maps of the table 'cmap' is coded in several formats.
     *  Format 6 is a trimmed table mapping. It is similar to format 0 but can have
     *  less than 256 entries.
     * @return a <CODE>HashMap</CODE> representing this map
     * @throws IOException the font file could not be read
     */
HashMap<Integer, int[]> readFormat6() throws IOException {
    HashMap<Integer, int[]> h = new HashMap<Integer, int[]>();
    rf.skipBytes(4);
    int start_code = rf.readUnsignedShort();
    int code_count = rf.readUnsignedShort();
    for (int k = 0; k < code_count; ++k) {
        int r[] = new int[2];
        r[0] = rf.readUnsignedShort();
        r[1] = getGlyphWidth(r[0]);
        h.put(new Integer(k + start_code), r);
    }
    return h;
}
