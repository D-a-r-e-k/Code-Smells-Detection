/**
     * Reads an SVG "radialGradient" element.
     */
private void readRadialGradientElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    double cx = toLength(elem, readAttribute(elem, "cx", "0.5"), 0.01);
    double cy = toLength(elem, readAttribute(elem, "cy", "0.5"), 0.01);
    double fx = toLength(elem, readAttribute(elem, "fx", readAttribute(elem, "cx", "0.5")), 0.01);
    double fy = toLength(elem, readAttribute(elem, "fy", readAttribute(elem, "cy", "0.5")), 0.01);
    double r = toLength(elem, readAttribute(elem, "r", "0.5"), 0.01);
    boolean isRelativeToFigureBounds = readAttribute(elem, "gradientUnits", "objectBoundingBox").equals("objectBoundingBox");
    ArrayList<IXMLElement> stops = elem.getChildrenNamed("stop", SVG_NAMESPACE);
    if (stops.size() == 0) {
        stops = elem.getChildrenNamed("stop");
    }
    if (stops.size() == 0) {
        // FIXME - Implement xlink support throughout SVGInputFormat 
        String xlink = readAttribute(elem, "xlink:href", "");
        if (xlink.startsWith("#") && identifiedElements.get(xlink.substring(1)) != null) {
            stops = identifiedElements.get(xlink.substring(1)).getChildrenNamed("stop", SVG_NAMESPACE);
            if (stops.size() == 0) {
                stops = identifiedElements.get(xlink.substring(1)).getChildrenNamed("stop");
            }
        }
    }
    double[] stopOffsets = new double[stops.size()];
    Color[] stopColors = new Color[stops.size()];
    double[] stopOpacities = new double[stops.size()];
    for (int i = 0; i < stops.size(); i++) {
        IXMLElement stopElem = stops.get(i);
        String offsetStr = readAttribute(stopElem, "offset", "0");
        if (offsetStr.endsWith("%")) {
            stopOffsets[i] = toDouble(stopElem, offsetStr.substring(0, offsetStr.length() - 1), 0, 0, 100) / 100d;
        } else {
            stopOffsets[i] = toDouble(stopElem, offsetStr, 0, 0, 1);
        }
        // 'stop-color' 
        // Value:  	currentColor | <color> | inherit 
        // Initial:  	black 
        // Applies to:  	 'stop' elements 
        // Inherited:  	no 
        // Percentages:  	N/A 
        // Media:  	visual 
        // Animatable:  	yes 
        // Computed value:  	 Specified <color> value, except i 
        stopColors[i] = toColor(stopElem, readAttribute(stopElem, "stop-color", "black"));
        if (stopColors[i] == null) {
            stopColors[i] = new Color(0x0, true);
        }
        //'stop-opacity' 
        //Value:  	<opacity-value> | inherit 
        //Initial:  	1 
        //Applies to:  	 'stop' elements 
        //Inherited:  	no 
        //Percentages:  	N/A 
        //Media:  	visual 
        //Animatable:  	yes 
        //Computed value:  	 Specified value, except inherit 
        stopOpacities[i] = toDouble(stopElem, readAttribute(stopElem, "stop-opacity", "1"), 1, 0, 1);
    }
    AffineTransform tx = toTransform(elem, readAttribute(elem, "gradientTransform", "none"));
    Gradient gradient = factory.createRadialGradient(cx, cy, fx, fy, r, stopOffsets, stopColors, stopOpacities, isRelativeToFigureBounds, tx);
    elementObjects.put(elem, gradient);
}
