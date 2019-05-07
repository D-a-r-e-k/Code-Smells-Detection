protected void readPages() throws IOException {
    catalog = trailer.getAsDict(PdfName.ROOT);
    rootPages = catalog.getAsDict(PdfName.PAGES);
    pageRefs = new PageRefs(this);
}
