void addFieldResources() throws IOException {
    if (fieldTemplates.isEmpty())
        return;
    PdfDictionary catalog = reader.getCatalog();
    PdfDictionary acroForm = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
    if (acroForm == null) {
        acroForm = new PdfDictionary();
        catalog.put(PdfName.ACROFORM, acroForm);
        markUsed(catalog);
    }
    PdfDictionary dr = (PdfDictionary) PdfReader.getPdfObject(acroForm.get(PdfName.DR), acroForm);
    if (dr == null) {
        dr = new PdfDictionary();
        acroForm.put(PdfName.DR, dr);
        markUsed(acroForm);
    }
    markUsed(dr);
    for (PdfTemplate template : fieldTemplates) {
        PdfFormField.mergeResources(dr, (PdfDictionary) template.getResources(), this);
    }
    // if (dr.get(PdfName.ENCODING) == null) dr.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING); 
    PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
    if (fonts == null) {
        fonts = new PdfDictionary();
        dr.put(PdfName.FONT, fonts);
    }
    if (!fonts.contains(PdfName.HELV)) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.BASEFONT, PdfName.HELVETICA);
        dic.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING);
        dic.put(PdfName.NAME, PdfName.HELV);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
        fonts.put(PdfName.HELV, addToBody(dic).getIndirectReference());
    }
    if (!fonts.contains(PdfName.ZADB)) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.BASEFONT, PdfName.ZAPFDINGBATS);
        dic.put(PdfName.NAME, PdfName.ZADB);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
        fonts.put(PdfName.ZADB, addToBody(dic).getIndirectReference());
    }
    if (acroForm.get(PdfName.DA) == null) {
        acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
        markUsed(acroForm);
    }
}
