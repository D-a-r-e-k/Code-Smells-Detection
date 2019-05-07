/**
     * Adds a <CODE>PdfWriter</CODE> to the <CODE>PdfDocument</CODE>.
     *
     * @param writer the <CODE>PdfWriter</CODE> that writes everything
     *                     what is added to this document to an outputstream.
     * @throws DocumentException on error
     */
public void addWriter(PdfWriter writer) throws DocumentException {
    if (this.writer == null) {
        this.writer = writer;
        annotationsImp = new PdfAnnotationsImp(writer);
        return;
    }
    throw new DocumentException(MessageLocalization.getComposedMessage("you.can.only.add.a.writer.to.a.pdfdocument.once"));
}
