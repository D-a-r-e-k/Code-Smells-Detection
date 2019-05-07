void eliminateAcroformObjects() {
    PdfObject acro = reader.getCatalog().get(PdfName.ACROFORM);
    if (acro == null)
        return;
    PdfDictionary acrodic = (PdfDictionary) PdfReader.getPdfObject(acro);
    reader.killXref(acrodic.get(PdfName.XFA));
    acrodic.remove(PdfName.XFA);
    PdfObject iFields = acrodic.get(PdfName.FIELDS);
    if (iFields != null) {
        PdfDictionary kids = new PdfDictionary();
        kids.put(PdfName.KIDS, iFields);
        sweepKids(kids);
        PdfReader.killIndirect(iFields);
        acrodic.put(PdfName.FIELDS, new PdfArray());
    }
    acrodic.remove(PdfName.SIGFLAGS);
}
