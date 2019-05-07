/** Replaces all the local named links with the actual destinations. */
public void consolidateNamedDestinations() {
    if (consolidateNamedDestinations)
        return;
    consolidateNamedDestinations = true;
    HashMap<Object, PdfObject> names = getNamedDestination(true);
    if (names.isEmpty())
        return;
    for (int k = 1; k <= pageRefs.size(); ++k) {
        PdfDictionary page = pageRefs.getPageN(k);
        PdfObject annotsRef;
        PdfArray annots = (PdfArray) getPdfObject(annotsRef = page.get(PdfName.ANNOTS));
        int annotIdx = lastXrefPartial;
        releaseLastXrefPartial();
        if (annots == null) {
            pageRefs.releasePage(k);
            continue;
        }
        boolean commitAnnots = false;
        for (int an = 0; an < annots.size(); ++an) {
            PdfObject objRef = annots.getPdfObject(an);
            if (replaceNamedDestination(objRef, names) && !objRef.isIndirect())
                commitAnnots = true;
        }
        if (commitAnnots)
            setXrefPartialObject(annotIdx, annots);
        if (!commitAnnots || annotsRef.isIndirect())
            pageRefs.releasePage(k);
    }
    PdfDictionary outlines = (PdfDictionary) getPdfObjectRelease(catalog.get(PdfName.OUTLINES));
    if (outlines == null)
        return;
    iterateBookmarks(outlines.get(PdfName.FIRST), names);
}
