/**
     * Gets the width that the line will occupy after writing.
     * Only the width of the first line is returned.
     *
     * @param phrase the <CODE>Phrase</CODE> containing the line
     * @return the width of the line
     */
public static float getWidth(Phrase phrase) {
    return getWidth(phrase, PdfWriter.RUN_DIRECTION_NO_BIDI, 0);
}
