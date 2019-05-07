/**
     * Initializes a page.
     * <P>
     * If the footer/header is set, it is printed.
     * @throws DocumentException on error
     */
protected void initPage() throws DocumentException {
    // the pagenumber is incremented 
    pageN++;
    // initialization of some page objects 
    annotationsImp.resetAnnotations();
    pageResources = new PageResources();
    writer.resetContent();
    graphics = new PdfContentByte(writer);
    markPoint = 0;
    setNewPageSizeAndMargins();
    imageEnd = -1;
    indentation.imageIndentRight = 0;
    indentation.imageIndentLeft = 0;
    indentation.indentBottom = 0;
    indentation.indentTop = 0;
    currentHeight = 0;
    // backgroundcolors, etc... 
    thisBoxSize = new HashMap<String, PdfRectangle>(boxSize);
    if (pageSize.getBackgroundColor() != null || pageSize.hasBorders() || pageSize.getBorderColor() != null) {
        add(pageSize);
    }
    float oldleading = leading;
    int oldAlignment = alignment;
    pageEmpty = true;
    // if there is an image waiting to be drawn, draw it 
    try {
        if (imageWait != null) {
            add(imageWait);
            imageWait = null;
        }
    } catch (Exception e) {
        throw new ExceptionConverter(e);
    }
    leading = oldleading;
    alignment = oldAlignment;
    carriageReturn();
    PdfPageEvent pageEvent = writer.getPageEvent();
    if (pageEvent != null) {
        if (firstPageEvent) {
            pageEvent.onOpenDocument(writer, this);
        }
        pageEvent.onStartPage(writer, this);
    }
    firstPageEvent = false;
}
