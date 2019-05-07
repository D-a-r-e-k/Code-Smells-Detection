/** Gets the box size. Allowed names are: "crop", "trim", "art", "bleed" and "media".
     * @param index the page number. The first page is 1
     * @param boxName the box name
     * @return the box rectangle or null
     */
public Rectangle getBoxSize(int index, String boxName) {
    PdfDictionary page = pageRefs.getPageNRelease(index);
    PdfArray box = null;
    if (boxName.equals("trim"))
        box = (PdfArray) getPdfObjectRelease(page.get(PdfName.TRIMBOX));
    else if (boxName.equals("art"))
        box = (PdfArray) getPdfObjectRelease(page.get(PdfName.ARTBOX));
    else if (boxName.equals("bleed"))
        box = (PdfArray) getPdfObjectRelease(page.get(PdfName.BLEEDBOX));
    else if (boxName.equals("crop"))
        box = (PdfArray) getPdfObjectRelease(page.get(PdfName.CROPBOX));
    else if (boxName.equals("media"))
        box = (PdfArray) getPdfObjectRelease(page.get(PdfName.MEDIABOX));
    if (box == null)
        return null;
    return getNormalizedRectangle(box);
}
