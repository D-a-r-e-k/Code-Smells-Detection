/**
     * Removes all the fields from the document.
     */
public void removeFields() {
    pageRefs.resetReleasePage();
    for (int k = 1; k <= pageRefs.size(); ++k) {
        PdfDictionary page = pageRefs.getPageN(k);
        PdfArray annots = page.getAsArray(PdfName.ANNOTS);
        if (annots == null) {
            pageRefs.releasePage(k);
            continue;
        }
        for (int j = 0; j < annots.size(); ++j) {
            PdfObject obj = getPdfObjectRelease(annots.getPdfObject(j));
            if (obj == null || !obj.isDictionary())
                continue;
            PdfDictionary annot = (PdfDictionary) obj;
            if (PdfName.WIDGET.equals(annot.get(PdfName.SUBTYPE)))
                annots.remove(j--);
        }
        if (annots.isEmpty())
            page.remove(PdfName.ANNOTS);
        else
            pageRefs.releasePage(k);
    }
    catalog.remove(PdfName.ACROFORM);
    pageRefs.resetReleasePage();
}
