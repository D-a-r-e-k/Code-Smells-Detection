/**
     * Reads the tables 'head', 'hhea', 'OS/2' and 'post' filling several variables.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
void fillTables() throws DocumentException, IOException {
    int table_location[];
    table_location = tables.get("head");
    if (table_location == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "head", fileName + style));
    rf.seek(table_location[0] + 16);
    head.flags = rf.readUnsignedShort();
    head.unitsPerEm = rf.readUnsignedShort();
    rf.skipBytes(16);
    head.xMin = rf.readShort();
    head.yMin = rf.readShort();
    head.xMax = rf.readShort();
    head.yMax = rf.readShort();
    head.macStyle = rf.readUnsignedShort();
    table_location = tables.get("hhea");
    if (table_location == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "hhea", fileName + style));
    rf.seek(table_location[0] + 4);
    hhea.Ascender = rf.readShort();
    hhea.Descender = rf.readShort();
    hhea.LineGap = rf.readShort();
    hhea.advanceWidthMax = rf.readUnsignedShort();
    hhea.minLeftSideBearing = rf.readShort();
    hhea.minRightSideBearing = rf.readShort();
    hhea.xMaxExtent = rf.readShort();
    hhea.caretSlopeRise = rf.readShort();
    hhea.caretSlopeRun = rf.readShort();
    rf.skipBytes(12);
    hhea.numberOfHMetrics = rf.readUnsignedShort();
    table_location = tables.get("OS/2");
    if (table_location == null)
        throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", "OS/2", fileName + style));
    rf.seek(table_location[0]);
    int version = rf.readUnsignedShort();
    os_2.xAvgCharWidth = rf.readShort();
    os_2.usWeightClass = rf.readUnsignedShort();
    os_2.usWidthClass = rf.readUnsignedShort();
    os_2.fsType = rf.readShort();
    os_2.ySubscriptXSize = rf.readShort();
    os_2.ySubscriptYSize = rf.readShort();
    os_2.ySubscriptXOffset = rf.readShort();
    os_2.ySubscriptYOffset = rf.readShort();
    os_2.ySuperscriptXSize = rf.readShort();
    os_2.ySuperscriptYSize = rf.readShort();
    os_2.ySuperscriptXOffset = rf.readShort();
    os_2.ySuperscriptYOffset = rf.readShort();
    os_2.yStrikeoutSize = rf.readShort();
    os_2.yStrikeoutPosition = rf.readShort();
    os_2.sFamilyClass = rf.readShort();
    rf.readFully(os_2.panose);
    rf.skipBytes(16);
    rf.readFully(os_2.achVendID);
    os_2.fsSelection = rf.readUnsignedShort();
    os_2.usFirstCharIndex = rf.readUnsignedShort();
    os_2.usLastCharIndex = rf.readUnsignedShort();
    os_2.sTypoAscender = rf.readShort();
    os_2.sTypoDescender = rf.readShort();
    if (os_2.sTypoDescender > 0)
        os_2.sTypoDescender = (short) -os_2.sTypoDescender;
    os_2.sTypoLineGap = rf.readShort();
    os_2.usWinAscent = rf.readUnsignedShort();
    os_2.usWinDescent = rf.readUnsignedShort();
    os_2.ulCodePageRange1 = 0;
    os_2.ulCodePageRange2 = 0;
    if (version > 0) {
        os_2.ulCodePageRange1 = rf.readInt();
        os_2.ulCodePageRange2 = rf.readInt();
    }
    if (version > 1) {
        rf.skipBytes(2);
        os_2.sCapHeight = rf.readShort();
    } else
        os_2.sCapHeight = (int) (0.7 * head.unitsPerEm);
    table_location = tables.get("post");
    if (table_location == null) {
        italicAngle = -Math.atan2(hhea.caretSlopeRun, hhea.caretSlopeRise) * 180 / Math.PI;
        return;
    }
    rf.seek(table_location[0] + 4);
    short mantissa = rf.readShort();
    int fraction = rf.readUnsignedShort();
    italicAngle = mantissa + fraction / 16384.0d;
    underlinePosition = rf.readShort();
    underlineThickness = rf.readShort();
    isFixedPitch = rf.readInt() != 0;
}
