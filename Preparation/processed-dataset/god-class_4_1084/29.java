/**
     * Adds a named outline to the document .
     * @param outline the outline to be added
     * @param name the name of this local destination
     */
void addOutline(PdfOutline outline, String name) {
    localDestination(name, outline.getPdfDestination());
}
