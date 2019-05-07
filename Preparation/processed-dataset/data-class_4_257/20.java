/**
     * Writes all the lines to the text-object.
     *
     * @return the displacement that was caused
     * @throws DocumentException on error
     */
protected float flushLines() throws DocumentException {
    // checks if the ArrayList with the lines is not null 
    if (lines == null) {
        return 0;
    }
    // checks if a new Line has to be made. 
    if (line != null && line.size() > 0) {
        lines.add(line);
        line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
    }
    // checks if the ArrayList with the lines is empty 
    if (lines.isEmpty()) {
        return 0;
    }
    // initialization of some parameters 
    Object currentValues[] = new Object[2];
    PdfFont currentFont = null;
    float displacement = 0;
    Float lastBaseFactor = new Float(0);
    currentValues[1] = lastBaseFactor;
    // looping over all the lines 
    for (PdfLine l : lines) {
        float moveTextX = l.indentLeft() - indentLeft() + indentation.indentLeft + indentation.listIndentLeft + indentation.sectionIndentLeft;
        text.moveText(moveTextX, -l.height());
        // is the line preceded by a symbol? 
        if (l.listSymbol() != null) {
            ColumnText.showTextAligned(graphics, Element.ALIGN_LEFT, new Phrase(l.listSymbol()), text.getXTLM() - l.listIndent(), text.getYTLM(), 0);
        }
        currentValues[0] = currentFont;
        writeLineToContent(l, text, graphics, currentValues, writer.getSpaceCharRatio());
        currentFont = (PdfFont) currentValues[0];
        displacement += l.height();
        text.moveText(-moveTextX, 0);
    }
    lines = new ArrayList<PdfLine>();
    return displacement;
}
