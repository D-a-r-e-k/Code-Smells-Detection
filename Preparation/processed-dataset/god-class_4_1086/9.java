/**
     * Gets the rotated page from a page dictionary.
     * @param page the page dictionary
     * @return the rotated page
     */
public Rectangle getPageSizeWithRotation(PdfDictionary page) {
    Rectangle rect = getPageSize(page);
    int rotation = getPageRotation(page);
    while (rotation > 0) {
        rect = rect.rotate();
        rotation -= 90;
    }
    return rect;
}
