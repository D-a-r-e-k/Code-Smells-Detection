// endPrefixMapping(String,Augmentations) 
// 
// Protected methods 
// 
/** Returns an HTML element. */
protected HTMLElements.Element getElement(final QName elementName) {
    String name = elementName.rawname;
    if (fNamespaces && NamespaceBinder.XHTML_1_0_URI.equals(elementName.uri)) {
        int index = name.indexOf(':');
        if (index != -1) {
            name = name.substring(index + 1);
        }
    }
    return HTMLElements.getElement(name);
}
