/**
     * Reads an SVG "linearGradient" element.
     */
private void readLinearGradientElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    double x1 = toLength(elem, readAttribute(elem, "x1", "0"), 0.01);
    double y1 = toLength(elem, readAttribute(elem, "y1", "0"), 0.01);
    double x2 = toLength(elem, readAttribute(elem, "x2", "1"), 0.01);
    double y2 = toLength(elem, readAttribute(elem, "y2", "0"), 0.01);
    boolean isRelativeToFigureBounds = readAttribute(elem, "gradientUnits", "objectBoundingBox").equals("objectBoundingBox");
    ArrayList<IXMLElement> stops = elem.getChildrenNamed("stop", SVG_NAMESPACE);
    if (stops.size() == 0) {
        stops = elem.getChildrenNamed("stop");
    }
    if (stops.size() == 0) {
        // FIXME - Implement xlink support throughouth SVGInputFormat 
        String xlink = readAttribute(elem, "xlink:href", "");
        if (xlink.startsWith("#") && identifiedElements.get(xlink.substring(1)) != null) {
            stops = identifiedElements.get(xlink.substring(1)).getChildrenNamed("stop", SVG_NAMESPACE);
            if (stops.size() == 0) {
                stops = identifiedElements.get(xlink.substring(1)).getChildrenNamed("stop");
            }
        }
    }
    if (stops.size() == 0) {
        if (DEBUG) {
            System.out.println("SVGInpuFormat: Warning no stops in linearGradient " + elem);
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
    Gradient gradient = factory.createLinearGradient(x1, y1, x2, y2, stopOffsets, stopColors, stopOpacities, isRelativeToFigureBounds, tx);
    elementObjects.put(elem, gradient);
}
