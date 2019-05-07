/** Reads the glyphs widths. The widths are extracted from the table 'hmtx'.
     *  The glyphs are normalized to 1000 units.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
protected void readGlyphWidths() throws DocumentException, IOException {
    int table_location[];
    table_location = tables.get("hmtx");
    if (table_location == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "hmtx", fileName + style));
    rf.seek(table_location[0]);
    GlyphWidths = new int[hhea.numberOfHMetrics];
    for (int k = 0; k < hhea.numberOfHMetrics; ++k) {
        GlyphWidths[k] = rf.readUnsignedShort() * 1000 / head.unitsPerEm;
        rf.readUnsignedShort();
    }
}
