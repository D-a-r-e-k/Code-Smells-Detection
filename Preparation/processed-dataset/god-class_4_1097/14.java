/**
     * Reads an ODG element.
     */
private ODGFigure readElement(IXMLElement elem) throws IOException {
    /*
        Drawing Shapes
        This section describes drawing shapes that might occur within all kind of applications.
        <define name="shape">
        <choice>
        <ref name="draw-rect"/>
        <ref name="draw-line"/>
        <ref name="draw-polyline"/>
        <ref name="draw-polygon"/>
        <ref name="draw-regular-polygon"/>
        <ref name="draw-path"/>
        <ref name="draw-circle"/>
        <ref name="draw-ellipse"/>
        <ref name="draw-g"/>
        <ref name="draw-page-thumbnail"/>
        <ref name="draw-frame"/>
        <ref name="draw-measure"/>
        <ref name="draw-caption"/>
        <ref name="draw-connector"/>
        <ref name="draw-control"/>
        <ref name="dr3d-scene"/>
        <ref name="draw-custom-shape"/>
        </choice>
        </define>
         */
    ODGFigure f = null;
    if (elem.getNamespace() == null || elem.getNamespace().equals(DRAWING_NAMESPACE)) {
        String name = elem.getName();
        if (name.equals("caption")) {
            f = readCaptionElement(elem);
        } else if (name.equals("circle")) {
            f = readCircleElement(elem);
        } else if (name.equals("connector")) {
            f = readCircleElement(elem);
        } else if (name.equals("custom-shape")) {
            f = readCustomShapeElement(elem);
        } else if (name.equals("ellipse")) {
            f = readEllipseElement(elem);
        } else if (name.equals("frame")) {
            f = readFrameElement(elem);
        } else if (name.equals("g")) {
            f = readGElement(elem);
        } else if (name.equals("line")) {
            f = readLineElement(elem);
        } else if (name.equals("measure")) {
            f = readMeasureElement(elem);
        } else if (name.equals("path")) {
            f = readPathElement(elem);
        } else if (name.equals("polygon")) {
            f = readPolygonElement(elem);
        } else if (name.equals("polyline")) {
            f = readPolylineElement(elem);
        } else if (name.equals("rect")) {
            f = readRectElement(elem);
        } else if (name.equals("regularPolygon")) {
            f = readRegularPolygonElement(elem);
        } else {
            if (DEBUG) {
                System.out.println("ODGInputFormat.readElement(" + elem + ") not implemented.");
            }
        }
    }
    if (f != null) {
        if (f.isEmpty()) {
            if (DEBUG) {
                System.out.println("ODGInputFormat.readElement():null - discarded empty figure " + f);
            }
            return null;
        }
        if (DEBUG) {
            System.out.println("ODGInputFormat.readElement():" + f + ".");
        }
    }
    return f;
}
