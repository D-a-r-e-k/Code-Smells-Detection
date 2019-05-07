/* Reads font attributes as listed in
     * http://www.w3.org/TR/SVGMobile12/feature.html#Font
     */
private void readFontAttributes(IXMLElement elem, Map<AttributeKey, Object> a) throws IOException {
    String value;
    double doubleValue;
    // 'font-family' 
    // Value:  	[[ <family-name> | 
    // <generic-family> ],]* [<family-name> | 
    // <generic-family>] | inherit 
    // Initial:  	depends on user agent 
    // Applies to:  	text content elements 
    // Inherited:  	yes 
    // Percentages:  	N/A 
    // Media:  	visual 
    // Animatable:  	yes 
    // Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "font-family", "Dialog");
    String[] familyNames = toQuotedAndCommaSeparatedArray(value);
    Font font = null;
    // Try to find a font with exactly matching name 
    for (int i = 0; i < familyNames.length; i++) {
        try {
            font = (Font) fontFormatter.stringToValue(familyNames[i]);
            break;
        } catch (ParseException e) {
        }
    }
    if (font == null) {
        // Try to create a similar font using the first name in the list 
        if (familyNames.length > 0) {
            fontFormatter.setAllowsUnknownFont(true);
            try {
                font = (Font) fontFormatter.stringToValue(familyNames[0]);
            } catch (ParseException e) {
            }
            fontFormatter.setAllowsUnknownFont(false);
        }
    }
    if (font == null) {
        // Fallback to the system Dialog font 
        font = new Font("Dialog", Font.PLAIN, 12);
    }
    FONT_FACE.put(a, font);
    // 'font-getChildCount' 
    // Value:  	<absolute-getChildCount> | <relative-getChildCount> | 
    // <length> | inherit 
    // Initial:  	medium 
    // Applies to:  	text content elements 
    // Inherited:  	yes, the computed value is inherited 
    // Percentages:  	N/A 
    // Media:  	visual 
    // Animatable:  	yes 
    // Computed value:  	 Absolute length 
    doubleValue = readInheritFontSizeAttribute(elem, "font-size", "medium");
    FONT_SIZE.put(a, doubleValue);
    // 'font-style' 
    // Value:  	normal | italic | oblique | inherit 
    // Initial:  	normal 
    // Applies to:  	text content elements 
    // Inherited:  	yes 
    // Percentages:  	N/A 
    // Media:  	visual 
    // Animatable:  	yes 
    // Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "font-style", "normal");
    FONT_ITALIC.put(a, value.equals("italic"));
    //'font-variant' 
    //Value:  	normal | small-caps | inherit 
    //Initial:  	normal 
    //Applies to:  	text content elements 
    //Inherited:  	yes 
    //Percentages:  	N/A 
    //Media:  	visual 
    //Animatable:  	no 
    //Computed value:  	 Specified value, except inherit 
    value = readInheritAttribute(elem, "font-variant", "normal");
    // if (DEBUG) System.out.println("font-variant="+value); 
    // 'font-weight' 
    // Value:  	normal | bold | bolder | lighter | 100 | 200 | 300 
    // | 400 | 500 | 600 | 700 | 800 | 900 | inherit 
    // Initial:  	normal 
    // Applies to:  	text content elements 
    // Inherited:  	yes 
    // Percentages:  	N/A 
    // Media:  	visual 
    // Animatable:  	yes 
    // Computed value:  	 one of the legal numeric values, non-numeric 
    // values shall be converted to numeric values according to the rules 
    // defined below. 
    value = readInheritAttribute(elem, "font-weight", "normal");
    FONT_BOLD.put(a, value.equals("bold") || value.equals("bolder") || value.equals("400") || value.equals("500") || value.equals("600") || value.equals("700") || value.equals("800") || value.equals("900"));
    // Note: text-decoration is an SVG 1.1 feature 
    //'text-decoration' 
    //Value:  	none | [ underline || overline || line-through || blink ] | inherit 
    //Initial:  	none 
    //Applies to:  	text content elements 
    //Inherited:  	no (see prose) 
    //Percentages:  	N/A 
    //Media:  	visual 
    //Animatable:  	yes 
    value = readAttribute(elem, "text-decoration", "none");
    FONT_UNDERLINE.put(a, value.equals("underline"));
}
