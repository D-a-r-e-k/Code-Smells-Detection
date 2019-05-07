private ODGFigure readEnhancedGeometryElement(IXMLElement elem, Map<AttributeKey, Object> a, Rectangle2D.Double figureBounds) throws IOException {
    /* The <draw:enhanced-geometry> element contains the geometry for a
         * <draw:custom-shape> element if its draw:engine attribute has been
         * omitted.
         */
    /* The draw:type attribute contains the name of a shape type. This name
         * can be used to offer specialized user interfaces for certain classes
         * of shapes, like for arrows, smileys, etc.
         * The shape type is rendering engine dependent and does not influence
         * the geometry of the shape.
         * If the value of the draw:type attribute is non-primitive, then no
         * shape type is available.
         */
    String type = elem.getAttribute("type", DRAWING_NAMESPACE, "non-primitive");
    EnhancedPath path;
    if (elem.hasAttribute("enhanced-path", DRAWING_NAMESPACE)) {
        path = toEnhancedPath(elem.getAttribute("enhanced-path", DRAWING_NAMESPACE, null));
    } else {
        path = null;
    }
    /* The svg:viewBox attribute establishes a user coordinate system inside
         * the physical coordinate system of the shape specified by the position
         * and size attributes. This user coordinate system is used by the
         * <draw:enhanced-path> element.
         * The syntax for using this attribute is the same as the [SVG] syntax.
         * The value of the attribute are four numbers separated by white
         * spaces, which define the left, top, right, and bottom dimensions
         * of the user coordinate system.
         */
    String[] viewBoxValues = toWSOrCommaSeparatedArray(elem.getAttribute("viewBox", DRAWING_NAMESPACE, "0 0 100 100"));
    Rectangle2D.Double viewBox = new Rectangle2D.Double(toNumber(viewBoxValues[0]), toNumber(viewBoxValues[1]), toNumber(viewBoxValues[2]), toNumber(viewBoxValues[3]));
    AffineTransform viewTx = new AffineTransform();
    if (!viewBox.isEmpty()) {
        viewTx.scale(figureBounds.width / viewBox.width, figureBounds.height / viewBox.height);
        viewTx.translate(figureBounds.x - viewBox.x, figureBounds.y - viewBox.y);
    }
    /* The draw:mirror-vertical and draw:mirror-horizontal attributes
         * specify if the geometry of the shape is to be mirrored.
         */
    boolean mirrorVertical = elem.getAttribute("mirror-vertical", DRAWING_NAMESPACE, "false").equals("true");
    boolean mirrorHorizontal = elem.getAttribute("mirror-horizontal", DRAWING_NAMESPACE, "false").equals("true");
    // FIXME - Implement Text Rotate Angle 
    // FIXME - Implement Extrusion Allowed 
    // FIXME - Implement Text Path Allowed 
    // FIXME - Implement Concentric Gradient Allowed 
    ODGFigure figure;
    if (type.equals("rectangle")) {
        figure = createEnhancedGeometryRectangleFigure(figureBounds, a);
    } else if (type.equals("ellipse")) {
        figure = createEnhancedGeometryEllipseFigure(figureBounds, a);
    } else {
        System.out.println("ODGInputFormat.readEnhancedGeometryElement not implemented for " + elem);
        figure = null;
    }
    return figure;
}
