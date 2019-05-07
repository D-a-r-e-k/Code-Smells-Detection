/* Reads object/group opacity as described in
     * http://www.w3.org/TR/SVGMobile12/painting.html#groupOpacity
     */
private void readOpacityAttribute(IXMLElement elem, Map<AttributeKey, Object> a) throws IOException {
    //'opacity' 
    //Value:  	<opacity-value> | inherit 
    //Initial:  	1 
    //Applies to:  	 'image' element 
    //Inherited:  	no 
    //Percentages:  	N/A 
    //Media:  	visual 
    //Animatable:  	yes 
    //Computed value:  	 Specified value, except inherit 
    //<opacity-value> 
    //The uniform opacity setting must be applied across an entire object. 
    //Any values outside the range 0.0 (fully transparent) to 1.0 
    //(fully opaque) shall be clamped to this range. 
    //(See Clamping values which are restricted to a particular range.) 
    double value = toDouble(elem, readAttribute(elem, "opacity", "1"), 1, 0, 1);
    OPACITY.put(a, value);
}
