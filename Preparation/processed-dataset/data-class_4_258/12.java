private void readBbox() throws DocumentException, IOException {
    int tableLocation[];
    tableLocation = tables.get("head");
    if (tableLocation == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "head", fileName + style));
    rf.seek(tableLocation[0] + TrueTypeFontSubSet.HEAD_LOCA_FORMAT_OFFSET);
    boolean locaShortTable = rf.readUnsignedShort() == 0;
    tableLocation = tables.get("loca");
    if (tableLocation == null)
        return;
    rf.seek(tableLocation[0]);
    int locaTable[];
    if (locaShortTable) {
        int entries = tableLocation[1] / 2;
        locaTable = new int[entries];
        for (int k = 0; k < entries; ++k) locaTable[k] = rf.readUnsignedShort() * 2;
    } else {
        int entries = tableLocation[1] / 4;
        locaTable = new int[entries];
        for (int k = 0; k < entries; ++k) locaTable[k] = rf.readInt();
    }
    tableLocation = tables.get("glyf");
    if (tableLocation == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "glyf", fileName + style));
    int tableGlyphOffset = tableLocation[0];
    bboxes = new int[locaTable.length - 1][];
    for (int glyph = 0; glyph < locaTable.length - 1; ++glyph) {
        int start = locaTable[glyph];
        if (start != locaTable[glyph + 1]) {
            rf.seek(tableGlyphOffset + start + 2);
            bboxes[glyph] = new int[] { rf.readShort() * 1000 / head.unitsPerEm, rf.readShort() * 1000 / head.unitsPerEm, rf.readShort() * 1000 / head.unitsPerEm, rf.readShort() * 1000 / head.unitsPerEm };
        }
    }
}
