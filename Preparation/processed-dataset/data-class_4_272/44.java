/* Reads text flow attributes as listed in
     * http://www.w3.org/TR/SVGMobile12/feature.html#TextFlow
     */
private void readTextFlowAttributes(IXMLElement elem, HashMap<AttributeKey, Object> a) throws IOException {
    Object value;
    //'line-increment' 
    //Value:  	auto | <number> | inherit 
    //Initial:  	auto 
    //Applies to:  	'textArea' 
    //Inherited:  	yes 
    //Percentages:  	N/A 
    //Media:  	visual 
    //Animatable:  	yes 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "line-increment", "auto");
    if (DEBUG) {
        System.out.println("SVGInputFormat not implemented line-increment=" + value);
    }
}
