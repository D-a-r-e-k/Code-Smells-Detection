/**
     * Writes a text line to the document. It takes care of all the attributes.
     * <P>
     * Before entering the line position must have been established and the
     * <CODE>text</CODE> argument must be in text object scope (<CODE>beginText()</CODE>).
     * @param line the line to be written
     * @param text the <CODE>PdfContentByte</CODE> where the text will be written to
     * @param graphics the <CODE>PdfContentByte</CODE> where the graphics will be written to
     * @param currentValues the current font and extra spacing values
     * @param ratio
     * @throws DocumentException on error
     * @since 5.0.3 returns a float instead of void
     */
float writeLineToContent(PdfLine line, PdfContentByte text, PdfContentByte graphics, Object currentValues[], float ratio) throws DocumentException {
    PdfFont currentFont = (PdfFont) currentValues[0];
    float lastBaseFactor = ((Float) currentValues[1]).floatValue();
    PdfChunk chunk;
    int numberOfSpaces;
    int lineLen;
    boolean isJustified;
    float hangingCorrection = 0;
    float hScale = 1;
    float lastHScale = Float.NaN;
    float baseWordSpacing = 0;
    float baseCharacterSpacing = 0;
    float glueWidth = 0;
    float lastX = text.getXTLM() + line.getOriginalWidth();
    numberOfSpaces = line.numberOfSpaces();
    lineLen = line.getLineLengthUtf32();
    // does the line need to be justified? 
    isJustified = line.hasToBeJustified() && (numberOfSpaces != 0 || lineLen > 1);
    int separatorCount = line.getSeparatorCount();
    if (separatorCount > 0) {
        glueWidth = line.widthLeft() / separatorCount;
    } else if (isJustified && separatorCount == 0) {
        if (line.isNewlineSplit() && line.widthLeft() >= lastBaseFactor * (ratio * numberOfSpaces + lineLen - 1)) {
            if (line.isRTL()) {
                text.moveText(line.widthLeft() - lastBaseFactor * (ratio * numberOfSpaces + lineLen - 1), 0);
            }
            baseWordSpacing = ratio * lastBaseFactor;
            baseCharacterSpacing = lastBaseFactor;
        } else {
            float width = line.widthLeft();
            PdfChunk last = line.getChunk(line.size() - 1);
            if (last != null) {
                String s = last.toString();
                char c;
                if (s.length() > 0 && hangingPunctuation.indexOf((c = s.charAt(s.length() - 1))) >= 0) {
                    float oldWidth = width;
                    width += last.font().width(c) * 0.4f;
                    hangingCorrection = width - oldWidth;
                }
            }
            float baseFactor = width / (ratio * numberOfSpaces + lineLen - 1);
            baseWordSpacing = ratio * baseFactor;
            baseCharacterSpacing = baseFactor;
            lastBaseFactor = baseFactor;
        }
    } else if (line.alignment == Element.ALIGN_LEFT || line.alignment == Element.ALIGN_UNDEFINED) {
        lastX -= line.widthLeft();
    }
    int lastChunkStroke = line.getLastStrokeChunk();
    int chunkStrokeIdx = 0;
    float xMarker = text.getXTLM();
    float baseXMarker = xMarker;
    float yMarker = text.getYTLM();
    boolean adjustMatrix = false;
    float tabPosition = 0;
    // looping over all the chunks in 1 line 
    for (Iterator<PdfChunk> j = line.iterator(); j.hasNext(); ) {
        chunk = j.next();
        BaseColor color = chunk.color();
        float fontSize = chunk.font().size();
        float ascender = chunk.font().getFont().getFontDescriptor(BaseFont.ASCENT, fontSize);
        float descender = chunk.font().getFont().getFontDescriptor(BaseFont.DESCENT, fontSize);
        hScale = 1;
        if (chunkStrokeIdx <= lastChunkStroke) {
            float width;
            if (isJustified) {
                width = chunk.getWidthCorrected(baseCharacterSpacing, baseWordSpacing);
            } else {
                width = chunk.width();
            }
            if (chunk.isStroked()) {
                PdfChunk nextChunk = line.getChunk(chunkStrokeIdx + 1);
                if (chunk.isSeparator()) {
                    width = glueWidth;
                    Object[] sep = (Object[]) chunk.getAttribute(Chunk.SEPARATOR);
                    DrawInterface di = (DrawInterface) sep[0];
                    Boolean vertical = (Boolean) sep[1];
                    if (vertical.booleanValue()) {
                        di.draw(graphics, baseXMarker, yMarker + descender, baseXMarker + line.getOriginalWidth(), ascender - descender, yMarker);
                    } else {
                        di.draw(graphics, xMarker, yMarker + descender, xMarker + width, ascender - descender, yMarker);
                    }
                }
                if (chunk.isTab()) {
                    Object[] tab = (Object[]) chunk.getAttribute(Chunk.TAB);
                    DrawInterface di = (DrawInterface) tab[0];
                    tabPosition = ((Float) tab[1]).floatValue() + ((Float) tab[3]).floatValue();
                    if (tabPosition > xMarker) {
                        di.draw(graphics, xMarker, yMarker + descender, tabPosition, ascender - descender, yMarker);
                    }
                    float tmp = xMarker;
                    xMarker = tabPosition;
                    tabPosition = tmp;
                }
                if (chunk.isAttribute(Chunk.BACKGROUND)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.BACKGROUND))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    Object bgr[] = (Object[]) chunk.getAttribute(Chunk.BACKGROUND);
                    graphics.setColorFill((BaseColor) bgr[0]);
                    float extra[] = (float[]) bgr[1];
                    graphics.rectangle(xMarker - extra[0], yMarker + descender - extra[1] + chunk.getTextRise(), width - subtract + extra[0] + extra[2], ascender - descender + extra[1] + extra[3]);
                    graphics.fill();
                    graphics.setGrayFill(0);
                }
                if (chunk.isAttribute(Chunk.UNDERLINE)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.UNDERLINE))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    Object unders[][] = (Object[][]) chunk.getAttribute(Chunk.UNDERLINE);
                    BaseColor scolor = null;
                    for (int k = 0; k < unders.length; ++k) {
                        Object obj[] = unders[k];
                        scolor = (BaseColor) obj[0];
                        float ps[] = (float[]) obj[1];
                        if (scolor == null)
                            scolor = color;
                        if (scolor != null)
                            graphics.setColorStroke(scolor);
                        graphics.setLineWidth(ps[0] + fontSize * ps[1]);
                        float shift = ps[2] + fontSize * ps[3];
                        int cap2 = (int) ps[4];
                        if (cap2 != 0)
                            graphics.setLineCap(cap2);
                        graphics.moveTo(xMarker, yMarker + shift);
                        graphics.lineTo(xMarker + width - subtract, yMarker + shift);
                        graphics.stroke();
                        if (scolor != null)
                            graphics.resetGrayStroke();
                        if (cap2 != 0)
                            graphics.setLineCap(0);
                    }
                    graphics.setLineWidth(1);
                }
                if (chunk.isAttribute(Chunk.ACTION)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.ACTION))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    text.addAnnotation(new PdfAnnotation(writer, xMarker, yMarker + descender + chunk.getTextRise(), xMarker + width - subtract, yMarker + ascender + chunk.getTextRise(), (PdfAction) chunk.getAttribute(Chunk.ACTION)));
                }
                if (chunk.isAttribute(Chunk.REMOTEGOTO)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.REMOTEGOTO))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    Object obj[] = (Object[]) chunk.getAttribute(Chunk.REMOTEGOTO);
                    String filename = (String) obj[0];
                    if (obj[1] instanceof String)
                        remoteGoto(filename, (String) obj[1], xMarker, yMarker + descender + chunk.getTextRise(), xMarker + width - subtract, yMarker + ascender + chunk.getTextRise());
                    else
                        remoteGoto(filename, ((Integer) obj[1]).intValue(), xMarker, yMarker + descender + chunk.getTextRise(), xMarker + width - subtract, yMarker + ascender + chunk.getTextRise());
                }
                if (chunk.isAttribute(Chunk.LOCALGOTO)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.LOCALGOTO))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    localGoto((String) chunk.getAttribute(Chunk.LOCALGOTO), xMarker, yMarker, xMarker + width - subtract, yMarker + fontSize);
                }
                if (chunk.isAttribute(Chunk.LOCALDESTINATION)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.LOCALDESTINATION))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    localDestination((String) chunk.getAttribute(Chunk.LOCALDESTINATION), new PdfDestination(PdfDestination.XYZ, xMarker, yMarker + fontSize, 0));
                }
                if (chunk.isAttribute(Chunk.GENERICTAG)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.GENERICTAG))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    Rectangle rect = new Rectangle(xMarker, yMarker, xMarker + width - subtract, yMarker + fontSize);
                    PdfPageEvent pev = writer.getPageEvent();
                    if (pev != null)
                        pev.onGenericTag(writer, this, rect, (String) chunk.getAttribute(Chunk.GENERICTAG));
                }
                if (chunk.isAttribute(Chunk.PDFANNOTATION)) {
                    float subtract = lastBaseFactor;
                    if (nextChunk != null && nextChunk.isAttribute(Chunk.PDFANNOTATION))
                        subtract = 0;
                    if (nextChunk == null)
                        subtract += hangingCorrection;
                    PdfAnnotation annot = PdfFormField.shallowDuplicate((PdfAnnotation) chunk.getAttribute(Chunk.PDFANNOTATION));
                    annot.put(PdfName.RECT, new PdfRectangle(xMarker, yMarker + descender, xMarker + width - subtract, yMarker + ascender));
                    text.addAnnotation(annot);
                }
                float params[] = (float[]) chunk.getAttribute(Chunk.SKEW);
                Float hs = (Float) chunk.getAttribute(Chunk.HSCALE);
                if (params != null || hs != null) {
                    float b = 0, c = 0;
                    if (params != null) {
                        b = params[0];
                        c = params[1];
                    }
                    if (hs != null)
                        hScale = hs.floatValue();
                    text.setTextMatrix(hScale, b, c, 1, xMarker, yMarker);
                }
                if (chunk.isAttribute(Chunk.CHAR_SPACING)) {
                    Float cs = (Float) chunk.getAttribute(Chunk.CHAR_SPACING);
                    text.setCharacterSpacing(cs.floatValue());
                }
                if (chunk.isImage()) {
                    Image image = chunk.getImage();
                    float matrix[] = image.matrix();
                    matrix[Image.CX] = xMarker + chunk.getImageOffsetX() - matrix[Image.CX];
                    matrix[Image.CY] = yMarker + chunk.getImageOffsetY() - matrix[Image.CY];
                    graphics.addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
                    text.moveText(xMarker + lastBaseFactor + image.getScaledWidth() - text.getXTLM(), 0);
                }
            }
            xMarker += width;
            ++chunkStrokeIdx;
        }
        if (chunk.font().compareTo(currentFont) != 0) {
            currentFont = chunk.font();
            text.setFontAndSize(currentFont.getFont(), currentFont.size());
        }
        float rise = 0;
        Object textRender[] = (Object[]) chunk.getAttribute(Chunk.TEXTRENDERMODE);
        int tr = 0;
        float strokeWidth = 1;
        BaseColor strokeColor = null;
        Float fr = (Float) chunk.getAttribute(Chunk.SUBSUPSCRIPT);
        if (textRender != null) {
            tr = ((Integer) textRender[0]).intValue() & 3;
            if (tr != PdfContentByte.TEXT_RENDER_MODE_FILL)
                text.setTextRenderingMode(tr);
            if (tr == PdfContentByte.TEXT_RENDER_MODE_STROKE || tr == PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE) {
                strokeWidth = ((Float) textRender[1]).floatValue();
                if (strokeWidth != 1)
                    text.setLineWidth(strokeWidth);
                strokeColor = (BaseColor) textRender[2];
                if (strokeColor == null)
                    strokeColor = color;
                if (strokeColor != null)
                    text.setColorStroke(strokeColor);
            }
        }
        if (fr != null)
            rise = fr.floatValue();
        if (color != null)
            text.setColorFill(color);
        if (rise != 0)
            text.setTextRise(rise);
        if (chunk.isImage()) {
            adjustMatrix = true;
        } else if (chunk.isHorizontalSeparator()) {
            PdfTextArray array = new PdfTextArray();
            array.add(-glueWidth * 1000f / chunk.font.size() / hScale);
            text.showText(array);
        } else if (chunk.isTab()) {
            PdfTextArray array = new PdfTextArray();
            array.add((tabPosition - xMarker) * 1000f / chunk.font.size() / hScale);
            text.showText(array);
        } else if (isJustified && numberOfSpaces > 0 && chunk.isSpecialEncoding()) {
            if (hScale != lastHScale) {
                lastHScale = hScale;
                text.setWordSpacing(baseWordSpacing / hScale);
                text.setCharacterSpacing(baseCharacterSpacing / hScale + text.getCharacterSpacing());
            }
            String s = chunk.toString();
            int idx = s.indexOf(' ');
            if (idx < 0)
                text.showText(s);
            else {
                float spaceCorrection = -baseWordSpacing * 1000f / chunk.font.size() / hScale;
                PdfTextArray textArray = new PdfTextArray(s.substring(0, idx));
                int lastIdx = idx;
                while ((idx = s.indexOf(' ', lastIdx + 1)) >= 0) {
                    textArray.add(spaceCorrection);
                    textArray.add(s.substring(lastIdx, idx));
                    lastIdx = idx;
                }
                textArray.add(spaceCorrection);
                textArray.add(s.substring(lastIdx));
                text.showText(textArray);
            }
        } else {
            if (isJustified && hScale != lastHScale) {
                lastHScale = hScale;
                text.setWordSpacing(baseWordSpacing / hScale);
                text.setCharacterSpacing(baseCharacterSpacing / hScale + text.getCharacterSpacing());
            }
            text.showText(chunk.toString());
        }
        if (rise != 0)
            text.setTextRise(0);
        if (color != null)
            text.resetRGBColorFill();
        if (tr != PdfContentByte.TEXT_RENDER_MODE_FILL)
            text.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
        if (strokeColor != null)
            text.resetRGBColorStroke();
        if (strokeWidth != 1)
            text.setLineWidth(1);
        if (chunk.isAttribute(Chunk.SKEW) || chunk.isAttribute(Chunk.HSCALE)) {
            adjustMatrix = true;
            text.setTextMatrix(xMarker, yMarker);
        }
        if (chunk.isAttribute(Chunk.CHAR_SPACING)) {
            text.setCharacterSpacing(baseCharacterSpacing);
        }
    }
    if (isJustified) {
        text.setWordSpacing(0);
        text.setCharacterSpacing(0);
        if (line.isNewlineSplit())
            lastBaseFactor = 0;
    }
    if (adjustMatrix)
        text.moveText(baseXMarker - text.getXTLM(), 0);
    currentValues[0] = currentFont;
    currentValues[1] = new Float(lastBaseFactor);
    return lastX;
}
