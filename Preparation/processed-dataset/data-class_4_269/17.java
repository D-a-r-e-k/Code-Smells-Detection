/** A <draw:custom-shape> represents a shape that is capable of rendering
     * complex figures. It is offering font work and extrusion functiona-
     * lity. A custom shape may have a geometry that influences its shape.
     * This geometry may be visualized in office application user
     * interfaces, for instance by displaying interaction handles, that
     * provide a simple way to modify the geometry.
     */
private ODGFigure readCustomShapeElement(IXMLElement elem) throws IOException {
    String styleName = elem.getAttribute("style-name", DRAWING_NAMESPACE, null);
    Map<AttributeKey, Object> a = styles.getAttributes(styleName, "graphic");
    Rectangle2D.Double figureBounds = new Rectangle2D.Double(toLength(elem.getAttribute("x", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("y", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("width", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("height", SVG_NAMESPACE, "0"), 1));
    ODGFigure figure = null;
    for (IXMLElement child : elem.getChildrenNamed("enhanced-geometry", DRAWING_NAMESPACE)) {
        figure = readEnhancedGeometryElement(child, a, figureBounds);
    }
    return figure;
}
