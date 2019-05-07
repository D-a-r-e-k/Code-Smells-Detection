/* Reads viewport attributes.
     */
private void readViewportAttributes(IXMLElement elem, HashMap<AttributeKey, Object> a) throws IOException {
    Object value;
    Double doubleValue;
    // width of the viewport 
    value = readAttribute(elem, "width", null);
    if (DEBUG) {
        System.out.println("SVGInputFormat READ viewport w/h factors:" + viewportStack.peek().widthPercentFactor + "," + viewportStack.peek().heightPercentFactor);
    }
    if (value != null) {
        doubleValue = toLength(elem, (String) value, viewportStack.peek().widthPercentFactor);
        VIEWPORT_WIDTH.put(a, doubleValue);
    }
    // height of the viewport 
    value = readAttribute(elem, "height", null);
    if (value != null) {
        doubleValue = toLength(elem, (String) value, viewportStack.peek().heightPercentFactor);
        VIEWPORT_HEIGHT.put(a, doubleValue);
    }
    //'viewport-fill' 
    //Value:	 "none" | <color> | inherit 
    //Initial:	 none 
    //Applies to:	viewport-creating elements 
    //Inherited:	 no 
    //Percentages:	 N/A 
    //Media:	 visual 
    //Animatable:	 yes 
    //Computed value:  	 "none" or specified <color> value, except inherit 
    value = toPaint(elem, readInheritColorAttribute(elem, "viewport-fill", "none"));
    if (value == null || (value instanceof Color)) {
        VIEWPORT_FILL.put(a, (Color) value);
    }
    //'viewport-fill-opacity' 
    //Value:	<opacity-value> | inherit 
    //Initial:	 1.0 
    //Applies to:	viewport-creating elements 
    //Inherited:	 no 
    //Percentages:	 N/A 
    //Media:	 visual 
    //Animatable:	 yes 
    //Computed value:  	 Specified value, except inherit 
    doubleValue = toDouble(elem, readAttribute(elem, "viewport-fill-opacity", "1.0"));
    VIEWPORT_FILL_OPACITY.put(a, doubleValue);
}
