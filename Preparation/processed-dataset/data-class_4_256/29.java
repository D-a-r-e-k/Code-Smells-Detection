/**
     * @see com.itextpdf.text.pdf.PdfWriter#getPageReference(int)
     */
@Override
public PdfIndirectReference getPageReference(int page) {
    PdfIndirectReference ref = reader.getPageOrigRef(page);
    if (ref == null)
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", page));
    return ref;
}
