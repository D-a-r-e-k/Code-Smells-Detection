// Added in version 1.2 - Sets the text filter for the NDC 
protected void setNDCTextFilter(String text) {
    // if no value is set, set it to a blank string 
    // otherwise use the value provided 
    if (text == null) {
        _NDCTextFilter = "";
    } else {
        _NDCTextFilter = text;
    }
}
