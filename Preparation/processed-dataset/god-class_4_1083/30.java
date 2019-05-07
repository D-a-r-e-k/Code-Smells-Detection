/**
     * @see com.itextpdf.text.pdf.PdfWriter#addAnnotation(com.itextpdf.text.pdf.PdfAnnotation)
     */
@Override
public void addAnnotation(PdfAnnotation annot) {
    throw new RuntimeException(MessageLocalization.getComposedMessage("unsupported.in.this.context.use.pdfstamper.addannotation"));
}
