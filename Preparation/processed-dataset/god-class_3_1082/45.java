/**
     * Gets the width that the line will occupy after writing.
     * Only the width of the first line is returned.
     *
     * @param phrase the <CODE>Phrase</CODE> containing the line
     * @param runDirection the run direction
     * @param arabicOptions the options for the arabic shaping
     * @return the width of the line
     */
public static float getWidth(Phrase phrase, int runDirection, int arabicOptions) {
    ColumnText ct = new ColumnText(null);
    ct.addText(phrase);
    ct.addWaitingPhrase();
    PdfLine line = ct.bidiLine.processLine(0, 20000, Element.ALIGN_LEFT, runDirection, arabicOptions);
    if (line == null)
        return 0;
    else
        return 20000 - line.widthLeft();
}
