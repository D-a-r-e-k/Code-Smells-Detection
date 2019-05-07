/**
     * Gets the named destinations from the /Dests key in the catalog as an <CODE>HashMap</CODE>. The key is the name
     * and the value is the destinations array.
     * @param	keepNames	true if you want the keys to be real PdfNames instead of Strings
     * @return gets the named destinations
     * @since	2.1.6
     */
public HashMap<Object, PdfObject> getNamedDestinationFromNames(boolean keepNames) {
    HashMap<Object, PdfObject> names = new HashMap<Object, PdfObject>();
    if (catalog.get(PdfName.DESTS) != null) {
        PdfDictionary dic = (PdfDictionary) getPdfObjectRelease(catalog.get(PdfName.DESTS));
        if (dic == null)
            return names;
        Set<PdfName> keys = dic.getKeys();
        for (PdfName key : keys) {
            PdfArray arr = getNameArray(dic.get(key));
            if (arr == null)
                continue;
            if (keepNames) {
                names.put(key, arr);
            } else {
                String name = PdfName.decodeName(key.toString());
                names.put(name, arr);
            }
        }
    }
    return names;
}
