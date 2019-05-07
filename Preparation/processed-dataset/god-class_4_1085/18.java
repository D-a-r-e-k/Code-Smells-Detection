/** Reads the kerning information from the 'kern' table.
     * @throws IOException the font file could not be read
     */
void readKerning() throws IOException {
    int table_location[];
    table_location = tables.get("kern");
    if (table_location == null)
        return;
    rf.seek(table_location[0] + 2);
    int nTables = rf.readUnsignedShort();
    int checkpoint = table_location[0] + 4;
    int length = 0;
    for (int k = 0; k < nTables; ++k) {
        checkpoint += length;
        rf.seek(checkpoint);
        rf.skipBytes(2);
        length = rf.readUnsignedShort();
        int coverage = rf.readUnsignedShort();
        if ((coverage & 0xfff7) == 0x0001) {
            int nPairs = rf.readUnsignedShort();
            rf.skipBytes(6);
            for (int j = 0; j < nPairs; ++j) {
                int pair = rf.readInt();
                int value = rf.readShort() * 1000 / head.unitsPerEm;
                kerning.put(pair, value);
            }
        }
    }
}
