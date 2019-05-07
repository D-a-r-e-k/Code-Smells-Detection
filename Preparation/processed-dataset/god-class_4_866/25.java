// getDocumentHandler():XMLDocumentHandler 
// 
// Protected static methods 
// 
/** Returns the value of the specified attribute, ignoring case. */
protected static String getValue(XMLAttributes attrs, String aname) {
    int length = attrs != null ? attrs.getLength() : 0;
    for (int i = 0; i < length; i++) {
        if (attrs.getQName(i).equalsIgnoreCase(aname)) {
            return attrs.getValue(i);
        }
    }
    return null;
}
