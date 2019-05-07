protected PdfReaderInstance getPdfReaderInstance(PdfWriter writer) {
    return new PdfReaderInstance(this, writer);
}
