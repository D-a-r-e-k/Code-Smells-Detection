void deleteOutlines() {
    PdfDictionary catalog = reader.getCatalog();
    PRIndirectReference outlines = (PRIndirectReference) catalog.get(PdfName.OUTLINES);
    if (outlines == null)
        return;
    outlineTravel(outlines);
    PdfReader.killIndirect(outlines);
    catalog.remove(PdfName.OUTLINES);
    markUsed(catalog);
}
