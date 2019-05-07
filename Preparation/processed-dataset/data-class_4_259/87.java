/** Finds all the fonts not subset but embedded and marks them as subset.
     * @return the number of fonts altered
     */
public int createFakeFontSubsets() {
    int total = 0;
    for (int k = 1; k < xrefObj.size(); ++k) {
        PdfObject obj = getPdfObjectRelease(k);
        if (obj == null || !obj.isDictionary())
            continue;
        PdfDictionary dic = (PdfDictionary) obj;
        if (!existsName(dic, PdfName.TYPE, PdfName.FONT))
            continue;
        if (existsName(dic, PdfName.SUBTYPE, PdfName.TYPE1) || existsName(dic, PdfName.SUBTYPE, PdfName.MMTYPE1) || existsName(dic, PdfName.SUBTYPE, PdfName.TRUETYPE)) {
            String s = getSubsetPrefix(dic);
            if (s != null)
                continue;
            s = getFontName(dic);
            if (s == null)
                continue;
            String ns = BaseFont.createSubsetPrefix() + s;
            PdfDictionary fd = (PdfDictionary) getPdfObjectRelease(dic.get(PdfName.FONTDESCRIPTOR));
            if (fd == null)
                continue;
            if (fd.get(PdfName.FONTFILE) == null && fd.get(PdfName.FONTFILE2) == null && fd.get(PdfName.FONTFILE3) == null)
                continue;
            fd = dic.getAsDict(PdfName.FONTDESCRIPTOR);
            PdfName newName = new PdfName(ns);
            dic.put(PdfName.BASEFONT, newName);
            fd.put(PdfName.FONTNAME, newName);
            setXrefPartialObject(k, dic);
            ++total;
        }
    }
    return total;
}
