void method0() { 
/** The code pages possible for a True Type font.
     */
static final String codePages[] = { "1252 Latin 1", "1250 Latin 2: Eastern Europe", "1251 Cyrillic", "1253 Greek", "1254 Turkish", "1255 Hebrew", "1256 Arabic", "1257 Windows Baltic", "1258 Vietnamese", null, null, null, null, null, null, null, "874 Thai", "932 JIS/Japan", "936 Chinese: Simplified chars--PRC and Singapore", "949 Korean Wansung", "950 Chinese: Traditional chars--Taiwan and Hong Kong", "1361 Korean Johab", null, null, null, null, null, null, null, "Macintosh Character Set (US Roman)", "OEM Character Set", "Symbol Character Set", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "869 IBM Greek", "866 MS-DOS Russian", "865 MS-DOS Nordic", "864 Arabic", "863 MS-DOS Canadian French", "862 Hebrew", "861 MS-DOS Icelandic", "860 MS-DOS Portuguese", "857 IBM Turkish", "855 IBM Cyrillic; primarily Russian", "852 Latin 2", "775 MS-DOS Baltic", "737 Greek; former 437 G", "708 Arabic; ASMO 708", "850 WE/Latin 1", "437 US" };
protected boolean justNames = false;
/** Contains the location of the several tables. The key is the name of
     * the table and the value is an <CODE>int[2]</CODE> where position 0
     * is the offset from the start of the file and position 1 is the length
     * of the table.
     */
protected HashMap<String, int[]> tables;
/** The file in use.
     */
protected RandomAccessFileOrArray rf;
/** The file name.
     */
protected String fileName;
protected boolean cff = false;
protected int cffOffset;
protected int cffLength;
/** The offset from the start of the file to the table directory.
     * It is 0 for TTF and may vary for TTC depending on the chosen font.
     */
protected int directoryOffset;
/** The index for the TTC font. It is an empty <CODE>String</CODE> for a
     * TTF file.
     */
protected String ttcIndex;
/** The style modifier */
protected String style = "";
/** The content of table 'head'.
     */
protected FontHeader head = new FontHeader();
/** The content of table 'hhea'.
     */
protected HorizontalHeader hhea = new HorizontalHeader();
/** The content of table 'OS/2'.
     */
protected WindowsMetrics os_2 = new WindowsMetrics();
/** The width of the glyphs. This is essentially the content of table
     * 'hmtx' normalized to 1000 units.
     */
protected int GlyphWidths[];
protected int bboxes[][];
/** The map containing the code information for the table 'cmap', encoding 1.0.
     * The key is the code and the value is an <CODE>int[2]</CODE> where position 0
     * is the glyph number and position 1 is the glyph width normalized to 1000
     * units.
     */
protected HashMap<Integer, int[]> cmap10;
/** The map containing the code information for the table 'cmap', encoding 3.1
     * in Unicode.
     * <P>
     * The key is the code and the value is an <CODE>int</CODE>[2] where position 0
     * is the glyph number and position 1 is the glyph width normalized to 1000
     * units.
     */
protected HashMap<Integer, int[]> cmap31;
protected HashMap<Integer, int[]> cmapExt;
/** The map containing the kerning information. It represents the content of
     * table 'kern'. The key is an <CODE>Integer</CODE> where the top 16 bits
     * are the glyph number for the first character and the lower 16 bits are the
     * glyph number for the second character. The value is the amount of kerning in
     * normalized 1000 units as an <CODE>Integer</CODE>. This value is usually negative.
     */
protected IntHashtable kerning = new IntHashtable();
/**
     * The font name.
     * This name is usually extracted from the table 'name' with
     * the 'Name ID' 6.
     */
protected String fontName;
/** The full name of the font
     */
protected String fullName[][];
/** All the names of the Names-Table
     */
protected String allNameEntries[][];
/** The family name of the font
     */
protected String familyName[][];
/** The italic angle. It is usually extracted from the 'post' table or in it's
     * absence with the code:
     * <P>
     * <PRE>
     * -Math.atan2(hhea.caretSlopeRun, hhea.caretSlopeRise) * 180 / Math.PI
     * </PRE>
     */
protected double italicAngle;
/** <CODE>true</CODE> if all the glyphs have the same width.
     */
protected boolean isFixedPitch = false;
protected int underlinePosition;
protected int underlineThickness;
}
