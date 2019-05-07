/** Normalizes a <CODE>Rectangle</CODE> so that llx and lly are smaller than urx and ury.
     * @param box the original rectangle
     * @return a normalized <CODE>Rectangle</CODE>
     */
public static Rectangle getNormalizedRectangle(PdfArray box) {
    float llx = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(0))).floatValue();
    float lly = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(1))).floatValue();
    float urx = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(2))).floatValue();
    float ury = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(3))).floatValue();
    return new Rectangle(Math.min(llx, urx), Math.min(lly, ury), Math.max(llx, urx), Math.max(lly, ury));
}
