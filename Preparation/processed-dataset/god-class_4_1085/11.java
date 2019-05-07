/** Gets a glyph width.
     * @param glyph the glyph to get the width of
     * @return the width of the glyph in normalized 1000 units
     */
protected int getGlyphWidth(int glyph) {
    if (glyph >= GlyphWidths.length)
        glyph = GlyphWidths.length - 1;
    return GlyphWidths[glyph];
}
