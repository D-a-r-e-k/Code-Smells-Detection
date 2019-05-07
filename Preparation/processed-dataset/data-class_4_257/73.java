/**
     * Adds an image to the document.
     * @param image the <CODE>Image</CODE> to add
     * @throws PdfException on error
     * @throws DocumentException on error
     */
protected void add(Image image) throws PdfException, DocumentException {
    if (image.hasAbsoluteY()) {
        graphics.addImage(image);
        pageEmpty = false;
        return;
    }
    // if there isn't enough room for the image on this page, save it for the next page 
    if (currentHeight != 0 && indentTop() - currentHeight - image.getScaledHeight() < indentBottom()) {
        if (!strictImageSequence && imageWait == null) {
            imageWait = image;
            return;
        }
        newPage();
        if (currentHeight != 0 && indentTop() - currentHeight - image.getScaledHeight() < indentBottom()) {
            imageWait = image;
            return;
        }
    }
    pageEmpty = false;
    // avoid endless loops 
    if (image == imageWait)
        imageWait = null;
    boolean textwrap = (image.getAlignment() & Image.TEXTWRAP) == Image.TEXTWRAP && !((image.getAlignment() & Image.MIDDLE) == Image.MIDDLE);
    boolean underlying = (image.getAlignment() & Image.UNDERLYING) == Image.UNDERLYING;
    float diff = leading / 2;
    if (textwrap) {
        diff += leading;
    }
    float lowerleft = indentTop() - currentHeight - image.getScaledHeight() - diff;
    float mt[] = image.matrix();
    float startPosition = indentLeft() - mt[4];
    if ((image.getAlignment() & Image.RIGHT) == Image.RIGHT)
        startPosition = indentRight() - image.getScaledWidth() - mt[4];
    if ((image.getAlignment() & Image.MIDDLE) == Image.MIDDLE)
        startPosition = indentLeft() + (indentRight() - indentLeft() - image.getScaledWidth()) / 2 - mt[4];
    if (image.hasAbsoluteX())
        startPosition = image.getAbsoluteX();
    if (textwrap) {
        if (imageEnd < 0 || imageEnd < currentHeight + image.getScaledHeight() + diff) {
            imageEnd = currentHeight + image.getScaledHeight() + diff;
        }
        if ((image.getAlignment() & Image.RIGHT) == Image.RIGHT) {
            // indentation suggested by Pelikan Stephan 
            indentation.imageIndentRight += image.getScaledWidth() + image.getIndentationLeft();
        } else {
            // indentation suggested by Pelikan Stephan 
            indentation.imageIndentLeft += image.getScaledWidth() + image.getIndentationRight();
        }
    } else {
        if ((image.getAlignment() & Image.RIGHT) == Image.RIGHT)
            startPosition -= image.getIndentationRight();
        else if ((image.getAlignment() & Image.MIDDLE) == Image.MIDDLE)
            startPosition += image.getIndentationLeft() - image.getIndentationRight();
        else
            startPosition += image.getIndentationLeft();
    }
    graphics.addImage(image, mt[0], mt[1], mt[2], mt[3], startPosition, lowerleft - mt[5]);
    if (!(textwrap || underlying)) {
        currentHeight += image.getScaledHeight() + diff;
        flushLines();
        text.moveText(0, -(image.getScaledHeight() + diff));
        newLine();
    }
}
