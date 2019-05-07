/**
     * Reads an SVG "defs" element.
     */
private void readDefsElement(IXMLElement elem) throws IOException {
    for (IXMLElement node : elem.getChildren()) {
        if (node instanceof IXMLElement) {
            IXMLElement child = (IXMLElement) node;
            Figure childFigure = readElement(child);
        }
    }
}
