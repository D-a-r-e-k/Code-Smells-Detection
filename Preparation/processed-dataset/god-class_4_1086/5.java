/** Returns the document's acroform, if it has one.
     * @return the document's acroform
     */
public PRAcroForm getAcroForm() {
    if (!acroFormParsed) {
        acroFormParsed = true;
        PdfObject form = catalog.get(PdfName.ACROFORM);
        if (form != null) {
            try {
                acroForm = new PRAcroForm(this);
                acroForm.readAcroForm((PdfDictionary) getPdfObject(form));
            } catch (Exception e) {
                acroForm = null;
            }
        }
    }
    return acroForm;
}
