/**
     * Gets the global document JavaScript.
     * @param file the document file
     * @throws IOException on error
     * @return the global document JavaScript
     */
public String getJavaScript(RandomAccessFileOrArray file) throws IOException {
    PdfDictionary names = (PdfDictionary) getPdfObjectRelease(catalog.get(PdfName.NAMES));
    if (names == null)
        return null;
    PdfDictionary js = (PdfDictionary) getPdfObjectRelease(names.get(PdfName.JAVASCRIPT));
    if (js == null)
        return null;
    HashMap<String, PdfObject> jscript = PdfNameTree.readTree(js);
    String sortedNames[] = new String[jscript.size()];
    sortedNames = jscript.keySet().toArray(sortedNames);
    Arrays.sort(sortedNames);
    StringBuffer buf = new StringBuffer();
    for (int k = 0; k < sortedNames.length; ++k) {
        PdfDictionary j = (PdfDictionary) getPdfObjectRelease(jscript.get(sortedNames[k]));
        if (j == null)
            continue;
        PdfObject obj = getPdfObjectRelease(j.get(PdfName.JS));
        if (obj != null) {
            if (obj.isString())
                buf.append(((PdfString) obj).toUnicodeString()).append('\n');
            else if (obj.isStream()) {
                byte bytes[] = getStreamBytes((PRStream) obj, file);
                if (bytes.length >= 2 && bytes[0] == (byte) 254 && bytes[1] == (byte) 255)
                    buf.append(PdfEncodings.convertToString(bytes, PdfObject.TEXT_UNICODE));
                else
                    buf.append(PdfEncodings.convertToString(bytes, PdfObject.TEXT_PDFDOCENCODING));
                buf.append('\n');
            }
        }
    }
    return buf.toString();
}
