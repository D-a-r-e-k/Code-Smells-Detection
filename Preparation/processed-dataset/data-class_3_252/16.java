/**
     * Reuses an existing image.
     * @param ref the reference to the image dictionary
     * @throws BadElementException on error
     * @return the image
     */
public static Image getInstance(PRIndirectReference ref) throws BadElementException {
    PdfDictionary dic = (PdfDictionary) PdfReader.getPdfObjectRelease(ref);
    int width = ((PdfNumber) PdfReader.getPdfObjectRelease(dic.get(PdfName.WIDTH))).intValue();
    int height = ((PdfNumber) PdfReader.getPdfObjectRelease(dic.get(PdfName.HEIGHT))).intValue();
    Image imask = null;
    PdfObject obj = dic.get(PdfName.SMASK);
    if (obj != null && obj.isIndirect()) {
        imask = getInstance((PRIndirectReference) obj);
    } else {
        obj = dic.get(PdfName.MASK);
        if (obj != null && obj.isIndirect()) {
            PdfObject obj2 = PdfReader.getPdfObjectRelease(obj);
            if (obj2 instanceof PdfDictionary)
                imask = getInstance((PRIndirectReference) obj);
        }
    }
    Image img = new ImgRaw(width, height, 1, 1, null);
    img.imageMask = imask;
    img.directReference = ref;
    return img;
}
