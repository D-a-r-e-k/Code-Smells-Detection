/** Returns the content of the document information dictionary as a <CODE>HashMap</CODE>
     * of <CODE>String</CODE>.
     * @return content of the document information dictionary
     */
public HashMap<String, String> getInfo() {
    HashMap<String, String> map = new HashMap<String, String>();
    PdfDictionary info = trailer.getAsDict(PdfName.INFO);
    if (info == null)
        return map;
    for (Object element : info.getKeys()) {
        PdfName key = (PdfName) element;
        PdfObject obj = getPdfObject(info.get(key));
        if (obj == null)
            continue;
        String value = obj.toString();
        switch(obj.type()) {
            case PdfObject.STRING:
                {
                    value = ((PdfString) obj).toUnicodeString();
                    break;
                }
            case PdfObject.NAME:
                {
                    value = PdfName.decodeName(value);
                    break;
                }
        }
        map.put(PdfName.decodeName(key.toString()), value);
    }
    return map;
}
