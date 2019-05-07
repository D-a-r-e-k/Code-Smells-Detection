/** Extracts all the names of the names-Table
     * @throws DocumentException on error
     * @throws IOException on error
     */
String[][] getAllNames() throws DocumentException, IOException {
    int table_location[];
    table_location = tables.get("name");
    if (table_location == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "name", fileName + style));
    rf.seek(table_location[0] + 2);
    int numRecords = rf.readUnsignedShort();
    int startOfStorage = rf.readUnsignedShort();
    ArrayList<String[]> names = new ArrayList<String[]>();
    for (int k = 0; k < numRecords; ++k) {
        int platformID = rf.readUnsignedShort();
        int platformEncodingID = rf.readUnsignedShort();
        int languageID = rf.readUnsignedShort();
        int nameID = rf.readUnsignedShort();
        int length = rf.readUnsignedShort();
        int offset = rf.readUnsignedShort();
        int pos = rf.getFilePointer();
        rf.seek(table_location[0] + startOfStorage + offset);
        String name;
        if (platformID == 0 || platformID == 3 || platformID == 2 && platformEncodingID == 1) {
            name = readUnicodeString(length);
        } else {
            name = readStandardString(length);
        }
        names.add(new String[] { String.valueOf(nameID), String.valueOf(platformID), String.valueOf(platformEncodingID), String.valueOf(languageID), name });
        rf.seek(pos);
    }
    String thisName[][] = new String[names.size()][];
    for (int k = 0; k < names.size(); ++k) thisName[k] = names.get(k);
    return thisName;
}
