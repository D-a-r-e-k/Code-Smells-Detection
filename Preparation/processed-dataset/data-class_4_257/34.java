/**
     * Recursive method used to write outlines.
     */
void outlineTree(PdfOutline outline) throws IOException {
    outline.setIndirectReference(writer.getPdfIndirectReference());
    if (outline.parent() != null)
        outline.put(PdfName.PARENT, outline.parent().indirectReference());
    ArrayList<PdfOutline> kids = outline.getKids();
    int size = kids.size();
    for (int k = 0; k < size; ++k) outlineTree(kids.get(k));
    for (int k = 0; k < size; ++k) {
        if (k > 0)
            kids.get(k).put(PdfName.PREV, kids.get(k - 1).indirectReference());
        if (k < size - 1)
            kids.get(k).put(PdfName.NEXT, kids.get(k + 1).indirectReference());
    }
    if (size > 0) {
        outline.put(PdfName.FIRST, kids.get(0).indirectReference());
        outline.put(PdfName.LAST, kids.get(size - 1).indirectReference());
    }
    for (int k = 0; k < size; ++k) {
        PdfOutline kid = kids.get(k);
        writer.addToBody(kid, kid.indirectReference());
    }
}
