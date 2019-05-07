/**
     * Writes the outline tree to the body of the PDF document.
     */
void writeOutlines() throws IOException {
    if (rootOutline.getKids().size() == 0)
        return;
    outlineTree(rootOutline);
    writer.addToBody(rootOutline, rootOutline.indirectReference());
}
