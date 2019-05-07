/**
     * Reads an SVG "tspan" element.
     */
private void readTSpanElement(IXMLElement elem, DefaultStyledDocument doc) throws IOException {
    try {
        if (elem.getContent() != null) {
            doc.insertString(doc.getLength(), toText(elem, elem.getContent()), null);
        } else {
            for (IXMLElement node : elem.getChildren()) {
                if (node instanceof IXMLElement) {
                    IXMLElement child = (IXMLElement) node;
                    if (node.getName() != null && node.getName().equals("tspan")) {
                        readTSpanElement((IXMLElement) node, doc);
                    } else {
                        if (DEBUG) {
                            System.out.println("SVGInputFormat unknown text node " + node.getName());
                        }
                    }
                } else {
                    if (node.getName() == null) {
                        doc.insertString(doc.getLength(), toText(elem, node.getContent()), null);
                    }
                }
            }
        }
    } catch (BadLocationException e) {
        InternalError ex = new InternalError(e.getMessage());
        ex.initCause(e);
        throw ex;
    }
}
