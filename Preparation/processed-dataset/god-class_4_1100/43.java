/* Reads text attributes as listed in
     * http://www.w3.org/TR/SVGMobile12/feature.html#Text
     */
private void readTextAttributes(IXMLElement elem, Map<AttributeKey, Object> a) throws IOException {
    Object value;
    //'text-anchor' 
    //Value:  	start | middle | end | inherit 
    //Initial:  	start 
    //Applies to:  	'text' IXMLElement 
    //Inherited:  	yes 
    //Percentages:  	N/A 
    //Media:  	visual 
    //Animatable:  	yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "text-anchor", "start");
    if (SVG_TEXT_ANCHORS.get(value) != null) {
        TEXT_ANCHOR.put(a, SVG_TEXT_ANCHORS.get(value));
    }
    //'display-align' 
    //Value:  	auto | before | center | after | inherit 
    //Initial:  	auto 
    //Applies to:  	'textArea' 
    //Inherited:  	yes 
    //Percentages:  	N/A 
    //Media:  	visual 
    //Animatable:  	yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "display-align", "auto");
    // XXX - Implement me properly 
    if (!value.equals("auto")) {
        if (value.equals("center")) {
            TEXT_ANCHOR.put(a, TextAnchor.MIDDLE);
        } else if (value.equals("before")) {
            TEXT_ANCHOR.put(a, TextAnchor.END);
        }
    }
    //text-align 
    //Value:	 start | end | center | inherit 
    //Initial:	 start 
    //Applies to:	 textArea elements 
    //Inherited:	 yes 
    //Percentages:	 N/A 
    //Media:	 visual 
    //Animatable:	 yes 
    value = readInheritAttribute(elem, "text-align", "start");
    // XXX - Implement me properly 
    if (!value.equals("start")) {
        TEXT_ALIGN.put(a, SVG_TEXT_ALIGNS.get(value));
    }
}
