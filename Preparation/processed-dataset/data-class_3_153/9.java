protected void init() throws NoGlyphException {
    //--Figure out the indices and advances 
    char[] codes = chars.toCharArray();
    indices = new int[codes.length];
    advances = new int[codes.length];
    double maxAscent = 0.0;
    double maxDescent = 0.0;
    double scale = size * SWFConstants.TWIPS / 1024.0;
    for (int i = 0; i < codes.length; i++) {
        int code = (int) codes[i];
        int[] index = new int[1];
        FontDefinition.Glyph glyph = getGlyph(code, index);
        indices[i] = index[0];
        if (glyph != null) {
            Shape shape = glyph.getShape();
            double[] outline = shape.getBoundingRectangle();
            double x1 = outline[0] * scale;
            double y1 = outline[1] * scale;
            double x2 = outline[2] * scale;
            double y2 = outline[3] * scale;
            if (maxAscent < -y1)
                maxAscent = -y1;
            if (maxDescent < y2)
                maxDescent = y2;
            double advance = glyph.getAdvance() * scale;
            if (advance == 0)
                advance = x2 - x1;
            //Kerning adjustment 
            if (i < codes.length - 1) {
                advance += (fontDef.getKerningOffset(code, (int) codes[i + 1]) * scale);
            }
            totalAdvance += advance;
            advances[i] = (int) (advance * SWFConstants.TWIPS);
            if (i == 0)
                leftMargin = -y1;
            if (i == codes.length - 1)
                rightMargin = x2 - advance;
        }
    }
    ascent = fontDef.getAscent() * scale;
    if (ascent == 0.0)
        ascent = maxAscent;
    descent = fontDef.getDescent() * scale;
    if (descent == 0.0)
        descent = maxDescent;
}
