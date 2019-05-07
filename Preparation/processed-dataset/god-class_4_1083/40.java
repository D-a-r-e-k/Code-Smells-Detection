/**
     * Adds or replaces the Collection Dictionary in the Catalog.
     * @param	collection	the new collection dictionary.
     */
void makePackage(PdfCollection collection) {
    PdfDictionary catalog = reader.getCatalog();
    catalog.put(PdfName.COLLECTION, collection);
}
