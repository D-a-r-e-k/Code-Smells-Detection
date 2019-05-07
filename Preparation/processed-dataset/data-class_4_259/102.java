protected static PdfDictionary duplicatePdfDictionary(PdfDictionary original, PdfDictionary copy, PdfReader newReader) {
    if (copy == null)
        copy = new PdfDictionary();
    for (Object element : original.getKeys()) {
        PdfName key = (PdfName) element;
        copy.put(key, duplicatePdfObject(original.get(key), newReader));
    }
    return copy;
}
