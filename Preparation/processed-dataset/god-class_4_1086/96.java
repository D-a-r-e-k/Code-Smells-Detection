public ArrayList<PdfAnnotation.PdfImportedLink> getLinks(int page) {
    pageRefs.resetReleasePage();
    ArrayList<PdfAnnotation.PdfImportedLink> result = new ArrayList<PdfAnnotation.PdfImportedLink>();
    PdfDictionary pageDic = pageRefs.getPageN(page);
    if (pageDic.get(PdfName.ANNOTS) != null) {
        PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
        for (int j = 0; j < annots.size(); ++j) {
            PdfDictionary annot = (PdfDictionary) getPdfObjectRelease(annots.getPdfObject(j));
            if (PdfName.LINK.equals(annot.get(PdfName.SUBTYPE))) {
                result.add(new PdfAnnotation.PdfImportedLink(annot));
            }
        }
    }
    pageRefs.releasePage(page);
    pageRefs.resetReleasePage();
    return result;
}
