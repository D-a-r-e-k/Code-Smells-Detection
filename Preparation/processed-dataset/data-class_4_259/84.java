static String getFontName(PdfDictionary dic) {
    if (dic == null)
        return null;
    PdfObject type = getPdfObjectRelease(dic.get(PdfName.BASEFONT));
    if (type == null || !type.isName())
        return null;
    return PdfName.decodeName(type.toString());
}
