private AffineTransform readViewBoxTransform(IXMLElement elem) throws IOException {
    AffineTransform tx = new AffineTransform();
    Rectangle2D.Double figureBounds = new Rectangle2D.Double(toLength(elem.getAttribute("x", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("y", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("width", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("height", SVG_NAMESPACE, "0"), 1));
    tx.translate(figureBounds.x, figureBounds.y);
    // The svg:viewBox attribute establishes a user coordinate system inside the physical coordinate 
    // system of the shape specified by the position and size attributes. This user coordinate system is 
    // used by the svg:points attribute and the <draw:path> element. 
    // The syntax for using this attribute is the same as the [SVG] syntax. The value of the attribute are 
    // four numbers separated by white spaces, which define the left, top, right, and bottom dimensions 
    // of the user coordinate system. 
    // Some implementations may ignore the view box attribute. The implied coordinate system then has 
    // its origin at the left, top corner of the shape, without any scaling relative to the shape. 
    String[] viewBoxValues = toWSOrCommaSeparatedArray(elem.getAttribute("viewBox", SVG_NAMESPACE, null));
    if (viewBoxValues.length == 4) {
        Rectangle2D.Double viewBox = new Rectangle2D.Double(toNumber(viewBoxValues[0]), toNumber(viewBoxValues[1]), toNumber(viewBoxValues[2]), toNumber(viewBoxValues[3]));
        if (!viewBox.isEmpty() && !figureBounds.isEmpty()) {
            tx.scale(figureBounds.width / viewBox.width, figureBounds.height / viewBox.height);
            tx.translate(-viewBox.x, -viewBox.y);
        }
    }
    return tx;
}
