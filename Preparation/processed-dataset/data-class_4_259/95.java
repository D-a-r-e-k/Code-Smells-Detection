/**
     * Removes all the annotations and fields from the document.
     */
public void removeAnnotations() {
    pageRefs.resetReleasePage();
    for (int k = 1; k <= pageRefs.size(); ++k) {
        PdfDictionary page = pageRefs.getPageN(k);
        if (page.get(PdfName.ANNOTS) == null)
            pageRefs.releasePage(k);
        else
            page.remove(PdfName.ANNOTS);
    }
    catalog.remove(PdfName.ACROFORM);
    pageRefs.resetReleasePage();
}
