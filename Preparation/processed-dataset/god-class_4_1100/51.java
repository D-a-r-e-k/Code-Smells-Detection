/* Reads graphics attributes as listed in
     * http://www.w3.org/TR/SVGMobile12/feature.html#GraphicsAttribute
     */
private void readGraphicsAttributes(IXMLElement elem, Figure f) throws IOException {
    Object value;
    // 'display' 
    // Value:  	 inline | block | list-item | 
    // run-in | compact | marker | 
    // table | inline-table | table-row-group | table-header-group | 
    // table-footer-group | table-row | table-column-group | table-column | 
    // table-cell | table-caption | none | inherit 
    // Initial:  	 inline 
    // Applies to:  	 'svg' , 'g' , 'switch' , 'a' , 'foreignObject' , 
    // graphics elements (including the text content block elements) and text 
    // sub-elements (for example, 'tspan' and 'a' ) 
    // Inherited:  	 no 
    // Percentages:  	 N/A 
    // Media:  	 all 
    // Animatable:  	 yes 
    // Computed value:  	 Specified value, except inherit 
    value = readAttribute(elem, "display", "inline");
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented display=" + value);
    }
    //'image-rendering' 
    //Value:  	 auto | optimizeSpeed | optimizeQuality | inherit 
    //Initial:  	 auto 
    //Applies to:  	 images 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "image-rendering", "auto");
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented image-rendering=" + value);
    }
    //'pointer-events' 
    //Value:  	boundingBox | visiblePainted | visibleFill | visibleStroke | visible | 
    //painted | fill | stroke | all | none | inherit 
    //Initial:  	visiblePainted 
    //Applies to:  	graphics elements 
    //Inherited:  	yes 
    //Percentages:  	N/A 
    //Media:  	visual 
    //Animatable:  	yes 
    //Computed value:  	Specified value, except inherit 
    value = readInheritAttribute(elem, "pointer-events", "visiblePainted");
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented pointer-events=" + value);
    }
    // 'shape-rendering' 
    //Value:  	 auto | optimizeSpeed | crispEdges | 
    //geometricPrecision | inherit 
    //Initial:  	 auto 
    //Applies to:  	 shapes 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "shape-rendering", "auto");
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented shape-rendering=" + value);
    }
    //'text-rendering' 
    //Value:  	 auto | optimizeSpeed | optimizeLegibility | 
    //geometricPrecision | inherit 
    //Initial:  	 auto 
    //Applies to:  	text content block elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "text-rendering", "auto");
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented text-rendering=" + value);
    }
    //'vector-effect' 
    //Value:  	 non-scaling-stroke | none | inherit 
    //Initial:  	 none 
    //Applies to:  	 graphics elements 
    //Inherited:  	 no 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    value = readAttribute(elem, "vector-effect", "none");
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented vector-effect=" + value);
    }
    //'visibility' 
    //Value:  	 visible | hidden | collapse | inherit 
    //Initial:  	 visible 
    //Applies to:  	 graphics elements (including the text content block 
    // elements) and text sub-elements (for example, 'tspan' and 'a' ) 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "visibility", null);
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented visibility=" + value);
    }
}
