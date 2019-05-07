void sweepKids(PdfObject obj) {
    PdfObject oo = PdfReader.killIndirect(obj);
    if (oo == null || !oo.isDictionary())
        return;
    PdfDictionary dic = (PdfDictionary) oo;
    PdfArray kids = (PdfArray) PdfReader.killIndirect(dic.get(PdfName.KIDS));
    if (kids == null)
        return;
    for (int k = 0; k < kids.size(); ++k) {
        sweepKids(kids.getPdfObject(k));
    }
}
