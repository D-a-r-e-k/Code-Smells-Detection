void addDocumentField(PdfIndirectReference ref) {
    PdfDictionary catalog = reader.getCatalog();
    PdfDictionary acroForm = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
    if (acroForm == null) {
        acroForm = new PdfDictionary();
        catalog.put(PdfName.ACROFORM, acroForm);
        markUsed(catalog);
    }
    PdfArray fields = (PdfArray) PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
    if (fields == null) {
        fields = new PdfArray();
        acroForm.put(PdfName.FIELDS, fields);
        markUsed(acroForm);
    }
    if (!acroForm.contains(PdfName.DA)) {
        acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
        markUsed(acroForm);
    }
    fields.add(ref);
    markUsed(fields);
}
