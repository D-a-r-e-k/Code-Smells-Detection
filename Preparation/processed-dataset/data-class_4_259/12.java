/** Gets the crop box without taking rotation into account. This
     * is the value of the /CropBox key. The crop box is the part
     * of the document to be displayed or printed. It usually is the same
     * as the media box but may be smaller. If the page doesn't have a crop
     * box the page size will be returned.
     * @param index the page number. The first page is 1
     * @return the crop box
     */
public Rectangle getCropBox(int index) {
    PdfDictionary page = pageRefs.getPageNRelease(index);
    PdfArray cropBox = (PdfArray) getPdfObjectRelease(page.get(PdfName.CROPBOX));
    if (cropBox == null)
        return getPageSize(page);
    return getNormalizedRectangle(cropBox);
}
