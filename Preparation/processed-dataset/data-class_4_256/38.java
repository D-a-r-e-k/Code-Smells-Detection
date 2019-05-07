void setJavaScript() throws IOException {
    HashMap<String, PdfObject> djs = pdf.getDocumentLevelJS();
    if (djs.isEmpty())
        return;
    PdfDictionary catalog = reader.getCatalog();
    PdfDictionary names = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES), catalog);
    if (names == null) {
        names = new PdfDictionary();
        catalog.put(PdfName.NAMES, names);
        markUsed(catalog);
    }
    markUsed(names);
    PdfDictionary tree = PdfNameTree.writeTree(djs, this);
    names.put(PdfName.JAVASCRIPT, addToBody(tree).getIndirectReference());
}
