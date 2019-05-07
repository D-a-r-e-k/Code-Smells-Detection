/** Reads the font data.
     * @param ttfAfm the font as a <CODE>byte</CODE> array, possibly <CODE>null</CODE>
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     * @since	2.1.5
     */
void process(byte ttfAfm[], boolean preload) throws DocumentException, IOException {
    tables = new HashMap<String, int[]>();
    try {
        if (ttfAfm == null)
            rf = new RandomAccessFileOrArray(fileName, preload, Document.plainRandomAccess);
        else
            rf = new RandomAccessFileOrArray(ttfAfm);
        if (ttcIndex.length() > 0) {
            int dirIdx = Integer.parseInt(ttcIndex);
            if (dirIdx < 0)
                throw new DocumentException(MessageLocalization.getComposedMessage("the.font.index.for.1.must.be.positive", fileName));
            String mainTag = readStandardString(4);
            if (!mainTag.equals("ttcf"))
                throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttc.file", fileName));
            rf.skipBytes(4);
            int dirCount = rf.readInt();
            if (dirIdx >= dirCount)
                throw new DocumentException(MessageLocalization.getComposedMessage("the.font.index.for.1.must.be.between.0.and.2.it.was.3", fileName, String.valueOf(dirCount - 1), String.valueOf(dirIdx)));
            rf.skipBytes(dirIdx * 4);
            directoryOffset = rf.readInt();
        }
        rf.seek(directoryOffset);
        int ttId = rf.readInt();
        if (ttId != 0x00010000 && ttId != 0x4F54544F)
            throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttf.or.otf.file", fileName));
        int num_tables = rf.readUnsignedShort();
        rf.skipBytes(6);
        for (int k = 0; k < num_tables; ++k) {
            String tag = readStandardString(4);
            rf.skipBytes(4);
            int table_location[] = new int[2];
            table_location[0] = rf.readInt();
            table_location[1] = rf.readInt();
            tables.put(tag, table_location);
        }
        checkCff();
        fontName = getBaseFont();
        fullName = getNames(4);
        //full name 
        familyName = getNames(1);
        //family name 
        allNameEntries = getAllNames();
        if (!justNames) {
            fillTables();
            readGlyphWidths();
            readCMaps();
            readKerning();
            readBbox();
            GlyphWidths = null;
        }
    } finally {
        if (rf != null) {
            rf.close();
            if (!embedded)
                rf = null;
        }
    }
}
