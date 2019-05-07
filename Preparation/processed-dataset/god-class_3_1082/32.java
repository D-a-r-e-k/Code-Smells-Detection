/**
     * Outputs the lines to the document. The output can be simulated.
     * @param simulate <CODE>true</CODE> to simulate the writing to the document
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     * @throws DocumentException on error
     */
public int go(boolean simulate) throws DocumentException {
    if (composite)
        return goComposite(simulate);
    addWaitingPhrase();
    if (bidiLine == null)
        return NO_MORE_TEXT;
    descender = 0;
    linesWritten = 0;
    lastX = 0;
    boolean dirty = false;
    float ratio = spaceCharRatio;
    Object currentValues[] = new Object[2];
    PdfFont currentFont = null;
    Float lastBaseFactor = new Float(0);
    currentValues[1] = lastBaseFactor;
    PdfDocument pdf = null;
    PdfContentByte graphics = null;
    PdfContentByte text = null;
    firstLineY = Float.NaN;
    int localRunDirection = PdfWriter.RUN_DIRECTION_NO_BIDI;
    if (runDirection != PdfWriter.RUN_DIRECTION_DEFAULT)
        localRunDirection = runDirection;
    if (canvas != null) {
        graphics = canvas;
        pdf = canvas.getPdfDocument();
        text = canvas.getDuplicate();
    } else if (!simulate)
        throw new NullPointerException(MessageLocalization.getComposedMessage("columntext.go.with.simulate.eq.eq.false.and.text.eq.eq.null"));
    if (!simulate) {
        if (ratio == GLOBAL_SPACE_CHAR_RATIO)
            ratio = text.getPdfWriter().getSpaceCharRatio();
        else if (ratio < 0.001f)
            ratio = 0.001f;
    }
    float firstIndent = 0;
    PdfLine line;
    float x1;
    int status = 0;
    while (true) {
        firstIndent = lastWasNewline ? indent : followingIndent;
        // 
        if (rectangularMode) {
            if (rectangularWidth <= firstIndent + rightIndent) {
                status = NO_MORE_COLUMN;
                if (bidiLine.isEmpty())
                    status |= NO_MORE_TEXT;
                break;
            }
            if (bidiLine.isEmpty()) {
                status = NO_MORE_TEXT;
                break;
            }
            line = bidiLine.processLine(leftX, rectangularWidth - firstIndent - rightIndent, alignment, localRunDirection, arabicOptions);
            if (line == null) {
                status = NO_MORE_TEXT;
                break;
            }
            float[] maxSize = line.getMaxSize();
            if (isUseAscender() && Float.isNaN(firstLineY))
                currentLeading = line.getAscender();
            else
                currentLeading = Math.max(fixedLeading + maxSize[0] * multipliedLeading, maxSize[1]);
            if (yLine > maxY || yLine - currentLeading < minY) {
                status = NO_MORE_COLUMN;
                bidiLine.restore();
                break;
            }
            yLine -= currentLeading;
            if (!simulate && !dirty) {
                text.beginText();
                dirty = true;
            }
            if (Float.isNaN(firstLineY))
                firstLineY = yLine;
            updateFilledWidth(rectangularWidth - line.widthLeft());
            x1 = leftX;
        } else {
            float yTemp = yLine;
            float xx[] = findLimitsTwoLines();
            if (xx == null) {
                status = NO_MORE_COLUMN;
                if (bidiLine.isEmpty())
                    status |= NO_MORE_TEXT;
                yLine = yTemp;
                break;
            }
            if (bidiLine.isEmpty()) {
                status = NO_MORE_TEXT;
                yLine = yTemp;
                break;
            }
            x1 = Math.max(xx[0], xx[2]);
            float x2 = Math.min(xx[1], xx[3]);
            if (x2 - x1 <= firstIndent + rightIndent)
                continue;
            if (!simulate && !dirty) {
                text.beginText();
                dirty = true;
            }
            line = bidiLine.processLine(x1, x2 - x1 - firstIndent - rightIndent, alignment, localRunDirection, arabicOptions);
            if (line == null) {
                status = NO_MORE_TEXT;
                yLine = yTemp;
                break;
            }
        }
        if (!simulate) {
            currentValues[0] = currentFont;
            text.setTextMatrix(x1 + (line.isRTL() ? rightIndent : firstIndent) + line.indentLeft(), yLine);
            lastX = pdf.writeLineToContent(line, text, graphics, currentValues, ratio);
            currentFont = (PdfFont) currentValues[0];
        }
        lastWasNewline = line.isNewlineSplit();
        yLine -= line.isNewlineSplit() ? extraParagraphSpace : 0;
        ++linesWritten;
        descender = line.getDescender();
    }
    if (dirty) {
        text.endText();
        canvas.add(text);
    }
    return status;
}
