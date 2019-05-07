/**
     * Gets the certification level for this document. The return values can be <code>PdfSignatureAppearance.NOT_CERTIFIED</code>,
     * <code>PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED</code>,
     * <code>PdfSignatureAppearance.CERTIFIED_FORM_FILLING</code> and
     * <code>PdfSignatureAppearance.CERTIFIED_FORM_FILLING_AND_ANNOTATIONS</code>.
     * <p>
     * No signature validation is made, use the methods available for that in <CODE>AcroFields</CODE>.
     * </p>
     * @return gets the certification level for this document
     */
public int getCertificationLevel() {
    PdfDictionary dic = catalog.getAsDict(PdfName.PERMS);
    if (dic == null)
        return PdfSignatureAppearance.NOT_CERTIFIED;
    dic = dic.getAsDict(PdfName.DOCMDP);
    if (dic == null)
        return PdfSignatureAppearance.NOT_CERTIFIED;
    PdfArray arr = dic.getAsArray(PdfName.REFERENCE);
    if (arr == null || arr.size() == 0)
        return PdfSignatureAppearance.NOT_CERTIFIED;
    dic = arr.getAsDict(0);
    if (dic == null)
        return PdfSignatureAppearance.NOT_CERTIFIED;
    dic = dic.getAsDict(PdfName.TRANSFORMPARAMS);
    if (dic == null)
        return PdfSignatureAppearance.NOT_CERTIFIED;
    PdfNumber p = dic.getAsNumber(PdfName.P);
    if (p == null)
        return PdfSignatureAppearance.NOT_CERTIFIED;
    return p.intValue();
}
