/**
     * Gives the size of a trim, art, crop or bleed box, or null if not defined.
     * @param boxName crop, trim, art or bleed
     */
Rectangle getBoxSize(String boxName) {
    PdfRectangle r = thisBoxSize.get(boxName);
    if (r != null)
        return r.getRectangle();
    return null;
}
