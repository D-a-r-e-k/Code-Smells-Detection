@Override
void addAnnotation(PdfAnnotation annot, int page) {
    annot.setPage(page);
    addAnnotation(annot, reader.getPageN(page));
}
