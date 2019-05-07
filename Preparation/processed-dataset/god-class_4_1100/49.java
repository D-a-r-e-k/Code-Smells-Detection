/** Reads line and polyline attributes.
     */
private void readLineAttributes(IXMLElement elem, HashMap<AttributeKey, Object> a) throws IOException {
    Object objectValue;
    String value;
    double doubleValue;
    //'color' 
    // Value:  	<color> | inherit 
    // Initial:  	 depends on user agent 
    // Applies to:  	None. Indirectly affects other properties via currentColor 
    // Inherited:  	 yes 
    // Percentages:  	 N/A 
    // Media:  	 visual 
    // Animatable:  	 yes 
    // Computed value:  	 Specified <color> value, except inherit 
    // 
    // value = readInheritAttribute(elem, "color", "black"); 
    // if (DEBUG) System.out.println("color="+value); 
    //'color-rendering' 
    // Value:  	 auto | optimizeSpeed | optimizeQuality | inherit 
    // Initial:  	 auto 
    // Applies to:  	 container elements , graphics elements and 'animateColor' 
    // Inherited:  	 yes 
    // Percentages:  	 N/A 
    // Media:  	 visual 
    // Animatable:  	 yes 
    // Computed value:  	 Specified value, except inherit 
    // 
    // value = readInheritAttribute(elem, "color-rendering", "auto"); 
    // if (DEBUG) System.out.println("color-rendering="+value); 
    // 'fill' 
    // Value:  	<paint> | inherit (See Specifying paint) 
    // Initial:  	 black 
    // Applies to:  	 shapes and text content elements 
    // Inherited:  	 yes 
    // Percentages:  	 N/A 
    // Media:  	 visual 
    // Animatable:  	 yes 
    // Computed value:  	 "none", system paint, specified <color> value or absolute IRI 
    objectValue = toPaint(elem, readInheritColorAttribute(elem, "fill", "none"));
    if (objectValue instanceof Color) {
        FILL_COLOR.put(a, (Color) objectValue);
    } else if (objectValue instanceof Gradient) {
        FILL_GRADIENT.putClone(a, (Gradient) objectValue);
    } else if (objectValue == null) {
        FILL_COLOR.put(a, null);
    } else {
        FILL_COLOR.put(a, null);
        if (DEBUG) {
            System.out.println("SVGInputFormat not implemented  fill=" + objectValue);
        }
    }
    //'fill-opacity' 
    //Value:  	 <opacity-value> | inherit 
    //Initial:  	 1 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    objectValue = readInheritAttribute(elem, "fill-opacity", "1");
    FILL_OPACITY.put(a, toDouble(elem, (String) objectValue, 1d, 0d, 1d));
    // 'fill-rule' 
    // Value:	 nonzero | evenodd | inherit 
    // Initial: 	 nonzero 
    // Applies to:  	 shapes and text content elements 
    // Inherited:  	 yes 
    // Percentages:  	 N/A 
    // Media:  	 visual 
    // Animatable:  	 yes 
    // Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "fill-rule", "nonzero");
    WINDING_RULE.put(a, SVG_FILL_RULES.get(value));
    //'stroke' 
    //Value:  	<paint> | inherit (See Specifying paint) 
    //Initial:  	 none 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 "none", system paint, specified <color> value 
    // or absolute IRI 
    objectValue = toPaint(elem, readInheritColorAttribute(elem, "stroke", "black"));
    if (objectValue instanceof Color) {
        STROKE_COLOR.put(a, (Color) objectValue);
    } else if (objectValue instanceof Gradient) {
        STROKE_GRADIENT.putClone(a, (Gradient) objectValue);
    } else if (objectValue == null) {
        STROKE_COLOR.put(a, null);
    } else {
        STROKE_COLOR.put(a, null);
        if (DEBUG) {
            System.out.println("SVGInputFormat not implemented  stroke=" + objectValue);
        }
    }
    //'stroke-dasharray' 
    //Value:  	 none | <dasharray> | inherit 
    //Initial:  	 none 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes (non-additive) 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "stroke-dasharray", "none");
    if (!value.equals("none")) {
        String[] values = toWSOrCommaSeparatedArray(value);
        double[] dashes = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            dashes[i] = toNumber(elem, values[i]);
        }
        STROKE_DASHES.put(a, dashes);
    }
    //'stroke-dashoffset' 
    //Value:  	<length> | inherit 
    //Initial:  	 0 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    doubleValue = toNumber(elem, readInheritAttribute(elem, "stroke-dashoffset", "0"));
    STROKE_DASH_PHASE.put(a, doubleValue);
    IS_STROKE_DASH_FACTOR.put(a, false);
    //'stroke-linecap' 
    //Value:  	 butt | round | square | inherit 
    //Initial:  	 butt 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "stroke-linecap", "butt");
    STROKE_CAP.put(a, SVG_STROKE_LINECAPS.get(value));
    //'stroke-linejoin' 
    //Value:  	 miter | round | bevel | inherit 
    //Initial:  	 miter 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "stroke-linejoin", "miter");
    STROKE_JOIN.put(a, SVG_STROKE_LINEJOINS.get(value));
    //'stroke-miterlimit' 
    //Value:  	 <miterlimit> | inherit 
    //Initial:  	 4 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    doubleValue = toDouble(elem, readInheritAttribute(elem, "stroke-miterlimit", "4"), 4d, 1d, Double.MAX_VALUE);
    STROKE_MITER_LIMIT.put(a, doubleValue);
    IS_STROKE_MITER_LIMIT_FACTOR.put(a, false);
    //'stroke-opacity' 
    //Value:  	 <opacity-value> | inherit 
    //Initial:  	 1 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    objectValue = readInheritAttribute(elem, "stroke-opacity", "1");
    STROKE_OPACITY.put(a, toDouble(elem, (String) objectValue, 1d, 0d, 1d));
    //'stroke-width' 
    //Value:  	<length> | inherit 
    //Initial:  	 1 
    //Applies to:  	 shapes and text content elements 
    //Inherited:  	 yes 
    //Percentages:  	 N/A 
    //Media:  	 visual 
    //Animatable:  	 yes 
    //Computed value:  	 Specified value, except inherit 
    doubleValue = toNumber(elem, readInheritAttribute(elem, "stroke-width", "1"));
    STROKE_WIDTH.put(a, doubleValue);
}
