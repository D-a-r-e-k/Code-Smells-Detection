/**
     * Gets the Postscript font name.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     * @return the Postscript font name
     */
String getBaseFont() throws DocumentException, IOException {
    int table_location[];
    table_location = tables.get("name");
    if (table_location == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "name", fileName + style));
    rf.seek(table_location[0] + 2);
    int numRecords = rf.readUnsignedShort();
    int startOfStorage = rf.readUnsignedShort();
    for (int k = 0; k < numRecords; ++k) {
        int platformID = rf.readUnsignedShort();
        int platformEncodingID = rf.readUnsignedShort();
        int languageID = rf.readUnsignedShort();
        int nameID = rf.readUnsignedShort();
        int length = rf.readUnsignedShort();
        int offset = rf.readUnsignedShort();
        if (nameID == 6) {
            rf.seek(table_location[0] + startOfStorage + offset);
            if (platformID == 0 || platformID == 3)
                return readUnicodeString(length);
            else
                return readStandardString(length);
        }
    }
    File file = new File(fileName);
    return file.getName().replace(' ', '-');
}
