private void outlineTravel(PRIndirectReference outline) {
    while (outline != null) {
        PdfDictionary outlineR = (PdfDictionary) PdfReader.getPdfObjectRelease(outline);
        PRIndirectReference first = (PRIndirectReference) outlineR.get(PdfName.FIRST);
        if (first != null) {
            outlineTravel(first);
        }
        PdfReader.killIndirect(outlineR.get(PdfName.DEST));
        PdfReader.killIndirect(outlineR.get(PdfName.A));
        PdfReader.killIndirect(outline);
        outline = (PRIndirectReference) outlineR.get(PdfName.NEXT);
    }
}
