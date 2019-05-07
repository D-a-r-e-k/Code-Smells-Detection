/**
     * Gets the named destinations from the /Dests key in the catalog as an <CODE>HashMap</CODE>. The key is the name
     * and the value is the destinations array.
     * @return gets the named destinations
     * @since 5.0.1 (generic type in signature)
     */
@SuppressWarnings("unchecked")
public HashMap<String, PdfObject> getNamedDestinationFromNames() {
    return new HashMap(getNamedDestinationFromNames(false));
}
