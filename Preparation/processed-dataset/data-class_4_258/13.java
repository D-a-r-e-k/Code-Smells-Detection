/** Reads the several maps from the table 'cmap'. The maps of interest are 1.0 for symbolic
     *  fonts and 3.1 for all others. A symbolic font is defined as having the map 3.0.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
void readCMaps() throws DocumentException, IOException {
    int table_location[];
    table_location = tables.get("cmap");
    if (table_location == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "cmap", fileName + style));
    rf.seek(table_location[0]);
    rf.skipBytes(2);
    int num_tables = rf.readUnsignedShort();
    fontSpecific = false;
    int map10 = 0;
    int map31 = 0;
    int map30 = 0;
    int mapExt = 0;
    for (int k = 0; k < num_tables; ++k) {
        int platId = rf.readUnsignedShort();
        int platSpecId = rf.readUnsignedShort();
        int offset = rf.readInt();
        if (platId == 3 && platSpecId == 0) {
            fontSpecific = true;
            map30 = offset;
        } else if (platId == 3 && platSpecId == 1) {
            map31 = offset;
        } else if (platId == 3 && platSpecId == 10) {
            mapExt = offset;
        }
        if (platId == 1 && platSpecId == 0) {
            map10 = offset;
        }
    }
    if (map10 > 0) {
        rf.seek(table_location[0] + map10);
        int format = rf.readUnsignedShort();
        switch(format) {
            case 0:
                cmap10 = readFormat0();
                break;
            case 4:
                cmap10 = readFormat4();
                break;
            case 6:
                cmap10 = readFormat6();
                break;
        }
    }
    if (map31 > 0) {
        rf.seek(table_location[0] + map31);
        int format = rf.readUnsignedShort();
        if (format == 4) {
            cmap31 = readFormat4();
        }
    }
    if (map30 > 0) {
        rf.seek(table_location[0] + map30);
        int format = rf.readUnsignedShort();
        if (format == 4) {
            cmap10 = readFormat4();
        }
    }
    if (mapExt > 0) {
        rf.seek(table_location[0] + mapExt);
        int format = rf.readUnsignedShort();
        switch(format) {
            case 0:
                cmapExt = readFormat0();
                break;
            case 4:
                cmapExt = readFormat4();
                break;
            case 6:
                cmapExt = readFormat6();
                break;
            case 12:
                cmapExt = readFormat12();
                break;
        }
    }
}
