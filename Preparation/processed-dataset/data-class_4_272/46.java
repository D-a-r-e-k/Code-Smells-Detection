/* Reads solid color attributes.
     */
private void readSolidColorElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    // 'solid-color' 
    //Value:	 currentColor | <color> | inherit 
    //Initial:	 black 
    //Applies to:	 'solidColor' elements 
    //Inherited:	 no 
    //Percentages:	 N/A 
    //Media:	 visual 
    //Animatable:	 yes 
    //Computed value:  	 Specified <color> value, except inherit 
    Color color = toColor(elem, readAttribute(elem, "solid-color", "black"));
    //'solid-opacity' 
    //Value:	<opacity-value> | inherit 
    //Initial:	 1 
    //Applies to:	 'solidColor' elements 
    //Inherited:	 no 
    //Percentages:	 N/A 
    //Media:	 visual 
    //Animatable:	 yes 
    //Computed value:  	 Specified value, except inherit 
    double opacity = toDouble(elem, readAttribute(elem, "solid-opacity", "1"), 1, 0, 1);
    if (opacity != 1) {
        color = new Color(((int) (255 * opacity) << 24) | (0xffffff & color.getRGB()), true);
    }
    elementObjects.put(elem, color);
}
