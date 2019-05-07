void setOutlines() throws IOException {
    if (newBookmarks == null)
        return;
    deleteOutlines();
    if (newBookmarks.isEmpty())
        return;
    PdfDictionary catalog = reader.getCatalog();
    boolean namedAsNames = catalog.get(PdfName.DESTS) != null;
    writeOutlines(catalog, namedAsNames);
    markUsed(catalog);
}
