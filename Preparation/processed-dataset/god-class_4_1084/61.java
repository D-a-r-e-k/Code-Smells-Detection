protected void setNewPageSizeAndMargins() {
    pageSize = nextPageSize;
    if (marginMirroring && (getPageNumber() & 1) == 0) {
        marginRight = nextMarginLeft;
        marginLeft = nextMarginRight;
    } else {
        marginLeft = nextMarginLeft;
        marginRight = nextMarginRight;
    }
    if (marginMirroringTopBottom && (getPageNumber() & 1) == 0) {
        marginTop = nextMarginBottom;
        marginBottom = nextMarginTop;
    } else {
        marginTop = nextMarginTop;
        marginBottom = nextMarginBottom;
    }
    text = new PdfContentByte(writer);
    text.reset();
    text.beginText();
    textEmptySize = text.size();
    // we move to the left/top position of the page 
    text.moveText(left(), top());
}
