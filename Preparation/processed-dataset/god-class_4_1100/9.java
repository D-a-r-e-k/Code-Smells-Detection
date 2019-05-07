/**
     * Reads an SVG element of any kind.
     * @return Returns the Figure, if the SVG element represents a Figure.
     * Returns null in all other cases.
     */
private Figure readElement(IXMLElement elem) throws IOException {
    if (DEBUG) {
        System.out.println("SVGInputFormat.readElement " + elem.getName() + " line:" + elem.getLineNr());
    }
    Figure f = null;
    if (elem.getNamespace() == null || elem.getNamespace().equals(SVG_NAMESPACE)) {
        String name = elem.getName();
        if (name == null) {
            if (DEBUG) {
                System.err.println("SVGInputFormat warning: skipping nameless element at line " + elem.getLineNr());
            }
        } else if (name.equals("a")) {
            f = readAElement(elem);
        } else if (name.equals("circle")) {
            f = readCircleElement(elem);
        } else if (name.equals("defs")) {
            readDefsElement(elem);
            f = null;
        } else if (name.equals("ellipse")) {
            f = readEllipseElement(elem);
        } else if (name.equals("g")) {
            f = readGElement(elem);
        } else if (name.equals("image")) {
            f = readImageElement(elem);
        } else if (name.equals("line")) {
            f = readLineElement(elem);
        } else if (name.equals("linearGradient")) {
            readLinearGradientElement(elem);
            f = null;
        } else if (name.equals("path")) {
            f = readPathElement(elem);
        } else if (name.equals("polygon")) {
            f = readPolygonElement(elem);
        } else if (name.equals("polyline")) {
            f = readPolylineElement(elem);
        } else if (name.equals("radialGradient")) {
            readRadialGradientElement(elem);
            f = null;
        } else if (name.equals("rect")) {
            f = readRectElement(elem);
        } else if (name.equals("solidColor")) {
            readSolidColorElement(elem);
            f = null;
        } else if (name.equals("svg")) {
            f = readSVGElement(elem);
        } else if (name.equals("switch")) {
            f = readSwitchElement(elem);
        } else if (name.equals("text")) {
            f = readTextElement(elem);
        } else if (name.equals("textArea")) {
            f = readTextAreaElement(elem);
        } else if (name.equals("title")) {
        } else if (name.equals("use")) {
            f = readUseElement(elem);
        } else if (name.equals("style")) {
        } else {
            if (DEBUG) {
                System.out.println("SVGInputFormat not implemented for <" + name + ">");
            }
        }
    }
    if (f instanceof SVGFigure) {
        if (((SVGFigure) f).isEmpty()) {
            // if (DEBUG) System.out.println("Empty figure "+f); 
            return null;
        }
    } else if (f != null) {
        if (DEBUG) {
            System.out.println("SVGInputFormat warning: not an SVGFigure " + f);
        }
    }
    return f;
}
