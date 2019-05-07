/**
     * Gets all the named destinations as an <CODE>HashMap</CODE>. The key is the name
     * and the value is the destinations array.
     * @return gets all the named destinations
     */
public HashMap<Object, PdfObject> getNamedDestination() {
    return getNamedDestination(false);
}
