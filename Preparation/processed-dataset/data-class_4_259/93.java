/**
     * Gets the named destinations from the /Names key in the catalog as an <CODE>HashMap</CODE>. The key is the name
     * and the value is the destinations array.
     * @return gets the named destinations
     */
public HashMap<String, PdfObject> getNamedDestinationFromStrings() {
    if (catalog.get(PdfName.NAMES) != null) {
        PdfDictionary dic = (PdfDictionary) getPdfObjectRelease(catalog.get(PdfName.NAMES));
        if (dic != null) {
            dic = (PdfDictionary) getPdfObjectRelease(dic.get(PdfName.DESTS));
            if (dic != null) {
                HashMap<String, PdfObject> names = PdfNameTree.readTree(dic);
                for (Iterator<Map.Entry<String, PdfObject>> it = names.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, PdfObject> entry = it.next();
                    PdfArray arr = getNameArray(entry.getValue());
                    if (arr != null)
                        entry.setValue(arr);
                    else
                        it.remove();
                }
                return names;
            }
        }
    }
    return new HashMap<String, PdfObject>();
}
