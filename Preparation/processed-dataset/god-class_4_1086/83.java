static boolean existsName(PdfDictionary dic, PdfName key, PdfName value) {
    PdfObject type = getPdfObjectRelease(dic.get(key));
    if (type == null || !type.isName())
        return false;
    PdfName name = (PdfName) type;
    return name.equals(value);
}
