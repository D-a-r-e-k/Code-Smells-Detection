/** Gets the font parameter identified by <CODE>key</CODE>. Valid values
     * for <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>
     * and <CODE>ITALICANGLE</CODE>.
     * @param key the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     */
@Override
public float getFontDescriptor(int key, float fontSize) {
    switch(key) {
        case ASCENT:
            return os_2.sTypoAscender * fontSize / head.unitsPerEm;
        case CAPHEIGHT:
            return os_2.sCapHeight * fontSize / head.unitsPerEm;
        case DESCENT:
            return os_2.sTypoDescender * fontSize / head.unitsPerEm;
        case ITALICANGLE:
            return (float) italicAngle;
        case BBOXLLX:
            return fontSize * head.xMin / head.unitsPerEm;
        case BBOXLLY:
            return fontSize * head.yMin / head.unitsPerEm;
        case BBOXURX:
            return fontSize * head.xMax / head.unitsPerEm;
        case BBOXURY:
            return fontSize * head.yMax / head.unitsPerEm;
        case AWT_ASCENT:
            return fontSize * hhea.Ascender / head.unitsPerEm;
        case AWT_DESCENT:
            return fontSize * hhea.Descender / head.unitsPerEm;
        case AWT_LEADING:
            return fontSize * hhea.LineGap / head.unitsPerEm;
        case AWT_MAXADVANCE:
            return fontSize * hhea.advanceWidthMax / head.unitsPerEm;
        case UNDERLINE_POSITION:
            return (underlinePosition - underlineThickness / 2) * fontSize / head.unitsPerEm;
        case UNDERLINE_THICKNESS:
            return underlineThickness * fontSize / head.unitsPerEm;
        case STRIKETHROUGH_POSITION:
            return os_2.yStrikeoutPosition * fontSize / head.unitsPerEm;
        case STRIKETHROUGH_THICKNESS:
            return os_2.yStrikeoutSize * fontSize / head.unitsPerEm;
        case SUBSCRIPT_SIZE:
            return os_2.ySubscriptYSize * fontSize / head.unitsPerEm;
        case SUBSCRIPT_OFFSET:
            return -os_2.ySubscriptYOffset * fontSize / head.unitsPerEm;
        case SUPERSCRIPT_SIZE:
            return os_2.ySuperscriptYSize * fontSize / head.unitsPerEm;
        case SUPERSCRIPT_OFFSET:
            return os_2.ySuperscriptYOffset * fontSize / head.unitsPerEm;
        case WEIGHT_CLASS:
            return os_2.usWeightClass;
        case WIDTH_CLASS:
            return os_2.usWidthClass;
    }
    return 0;
}
