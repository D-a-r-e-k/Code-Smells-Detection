/**
     * Reads an SVG "svg" element.
     */
private Figure readSVGElement(IXMLElement elem) throws IOException {
    // Establish a new viewport 
    Viewport viewport = new Viewport();
    String widthValue = readAttribute(elem, "width", "100%");
    String heightValue = readAttribute(elem, "height", "100%");
    viewport.width = toWidth(elem, widthValue);
    viewport.height = toHeight(elem, heightValue);
    if (readAttribute(elem, "viewBox", "none").equals("none")) {
        viewport.viewBox.width = viewport.width;
        viewport.viewBox.height = viewport.height;
    } else {
        String[] viewBoxValues = toWSOrCommaSeparatedArray(readAttribute(elem, "viewBox", "none"));
        viewport.viewBox.x = toNumber(elem, viewBoxValues[0]);
        viewport.viewBox.y = toNumber(elem, viewBoxValues[1]);
        viewport.viewBox.width = toNumber(elem, viewBoxValues[2]);
        viewport.viewBox.height = toNumber(elem, viewBoxValues[3]);
        // FIXME - Calculate percentages 
        if (widthValue.indexOf('%') > 0) {
            viewport.width = viewport.viewBox.width;
        }
        if (heightValue.indexOf('%') > 0) {
            viewport.height = viewport.viewBox.height;
        }
    }
    if (viewportStack.size() == 1) {
        // We always preserve the aspect ratio for to the topmost SVG element. 
        // This is not compliant, but looks much better. 
        viewport.isPreserveAspectRatio = true;
    } else {
        viewport.isPreserveAspectRatio = !readAttribute(elem, "preserveAspectRatio", "none").equals("none");
    }
    viewport.widthPercentFactor = viewport.viewBox.width / 100d;
    viewport.heightPercentFactor = viewport.viewBox.height / 100d;
    viewport.numberFactor = Math.min(viewport.width / viewport.viewBox.width, viewport.height / viewport.viewBox.height);
    AffineTransform viewBoxTransform = new AffineTransform();
    viewBoxTransform.translate(-viewport.viewBox.x * viewport.width / viewport.viewBox.width, -viewport.viewBox.y * viewport.height / viewport.viewBox.height);
    if (viewport.isPreserveAspectRatio) {
        double factor = Math.min(viewport.width / viewport.viewBox.width, viewport.height / viewport.viewBox.height);
        viewBoxTransform.scale(factor, factor);
    } else {
        viewBoxTransform.scale(viewport.width / viewport.viewBox.width, viewport.height / viewport.viewBox.height);
    }
    viewportStack.push(viewport);
    readViewportAttributes(elem, viewportStack.firstElement().attributes);
    // Read the figures 
    for (IXMLElement node : elem.getChildren()) {
        if (node instanceof IXMLElement) {
            IXMLElement child = (IXMLElement) node;
            Figure childFigure = readElement(child);
            // skip invisible elements 
            if (readAttribute(child, "visibility", "visible").equals("visible") && !readAttribute(child, "display", "inline").equals("none")) {
                if (childFigure != null) {
                    childFigure.transform(viewBoxTransform);
                    figures.add(childFigure);
                }
            }
        }
    }
    viewportStack.pop();
    return null;
}
