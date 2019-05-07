private void iterateBookmarks(PdfObject outlineRef, HashMap<Object, PdfObject> names) {
    while (outlineRef != null) {
        replaceNamedDestination(outlineRef, names);
        PdfDictionary outline = (PdfDictionary) getPdfObjectRelease(outlineRef);
        PdfObject first = outline.get(PdfName.FIRST);
        if (first != null) {
            iterateBookmarks(first, names);
        }
        outlineRef = outline.get(PdfName.NEXT);
    }
}
