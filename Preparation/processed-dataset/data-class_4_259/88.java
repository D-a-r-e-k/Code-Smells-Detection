private static PdfArray getNameArray(PdfObject obj) {
    if (obj == null)
        return null;
    obj = getPdfObjectRelease(obj);
    if (obj == null)
        return null;
    if (obj.isArray())
        return (PdfArray) obj;
    else if (obj.isDictionary()) {
        PdfObject arr2 = getPdfObjectRelease(((PdfDictionary) obj).get(PdfName.D));
        if (arr2 != null && arr2.isArray())
            return (PdfArray) arr2;
    }
    return null;
}
