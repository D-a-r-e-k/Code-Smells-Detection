/**
     * Replaces remote named links with local destinations that have the same name.
     * @since	5.0
     */
public void makeRemoteNamedDestinationsLocal() {
    if (remoteToLocalNamedDestinations)
        return;
    remoteToLocalNamedDestinations = true;
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
            if (convertNamedDestination(objRef, names) && !objRef.isIndirect())
                commitAnnots = true;
        }
        if (commitAnnots)
            setXrefPartialObject(annotIdx, annots);
        if (!commitAnnots || annotsRef.isIndirect())
            pageRefs.releasePage(k);
    }
}
