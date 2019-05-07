/**
     * Gets all the named destinations as an <CODE>HashMap</CODE>. The key is the name
     * and the value is the destinations array.
     * @param	keepNames	true if you want the keys to be real PdfNames instead of Strings
     * @return gets all the named destinations
     * @since	2.1.6
     */
public HashMap<Object, PdfObject> getNamedDestination(boolean keepNames) {
    HashMap<Object, PdfObject> names = getNamedDestinationFromNames(keepNames);
    names.putAll(getNamedDestinationFromStrings());
    return names;
}
