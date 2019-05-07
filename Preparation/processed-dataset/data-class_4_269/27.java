private ODGFigure readGElement(IXMLElement elem) throws IOException {
    CompositeFigure g = createGroupFigure();
    for (IXMLElement node : elem.getChildren()) {
        if (node instanceof IXMLElement) {
            IXMLElement child = (IXMLElement) node;
            Figure childFigure = readElement(child);
            if (childFigure != null) {
                g.basicAdd(childFigure);
            }
        }
    }
    /*
        readTransformAttribute(elem, a);
        if (TRANSFORM.get(a) != null) {
        g.transform(TRANSFORM.get(a));
        }*/
    return (ODGFigure) g;
}
