/**
     * Shows a line of text. Only the first line is written.
     *
     * @param canvas where the text is to be written to
     * @param alignment the alignment. It is not influenced by the run direction
     * @param phrase the <CODE>Phrase</CODE> with the text
     * @param x the x reference position
     * @param y the y reference position
     * @param rotation the rotation to be applied in degrees counterclockwise
     * @param runDirection the run direction
     * @param arabicOptions the options for the arabic shaping
     */
public static void showTextAligned(PdfContentByte canvas, int alignment, Phrase phrase, float x, float y, float rotation, int runDirection, int arabicOptions) {
    if (alignment != Element.ALIGN_LEFT && alignment != Element.ALIGN_CENTER && alignment != Element.ALIGN_RIGHT)
        alignment = Element.ALIGN_LEFT;
    canvas.saveState();
    ColumnText ct = new ColumnText(canvas);
    float lly = -1;
    float ury = 2;
    float llx;
    float urx;
    switch(alignment) {
        case Element.ALIGN_LEFT:
            llx = 0;
            urx = 20000;
            break;
        case Element.ALIGN_RIGHT:
            llx = -20000;
            urx = 0;
            break;
        default:
            llx = -20000;
            urx = 20000;
            break;
    }
    if (rotation == 0) {
        llx += x;
        lly += y;
        urx += x;
        ury += y;
    } else {
        double alpha = rotation * Math.PI / 180.0;
        float cos = (float) Math.cos(alpha);
        float sin = (float) Math.sin(alpha);
        canvas.concatCTM(cos, sin, -sin, cos, x, y);
    }
    ct.setSimpleColumn(phrase, llx, lly, urx, ury, 2, alignment);
    if (runDirection == PdfWriter.RUN_DIRECTION_RTL) {
        if (alignment == Element.ALIGN_LEFT)
            alignment = Element.ALIGN_RIGHT;
        else if (alignment == Element.ALIGN_RIGHT)
            alignment = Element.ALIGN_LEFT;
    }
    ct.setAlignment(alignment);
    ct.setArabicOptions(arabicOptions);
    ct.setRunDirection(runDirection);
    try {
        ct.go();
    } catch (DocumentException e) {
        throw new ExceptionConverter(e);
    }
    canvas.restoreState();
}
