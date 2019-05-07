/** Finds all the font subsets and changes the prefixes to some
     * random values.
     * @return the number of font subsets altered
     */
public int shuffleSubsetNames() {
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
            if (s == null)
                continue;
            String ns = BaseFont.createSubsetPrefix() + s.substring(7);
            PdfName newName = new PdfName(ns);
            dic.put(PdfName.BASEFONT, newName);
            setXrefPartialObject(k, dic);
            ++total;
            PdfDictionary fd = dic.getAsDict(PdfName.FONTDESCRIPTOR);
            if (fd == null)
                continue;
            fd.put(PdfName.FONTNAME, newName);
        } else if (existsName(dic, PdfName.SUBTYPE, PdfName.TYPE0)) {
            String s = getSubsetPrefix(dic);
            PdfArray arr = dic.getAsArray(PdfName.DESCENDANTFONTS);
            if (arr == null)
                continue;
            if (arr.isEmpty())
                continue;
            PdfDictionary desc = arr.getAsDict(0);
            String sde = getSubsetPrefix(desc);
            if (sde == null)
                continue;
            String ns = BaseFont.createSubsetPrefix();
            if (s != null)
                dic.put(PdfName.BASEFONT, new PdfName(ns + s.substring(7)));
            setXrefPartialObject(k, dic);
            PdfName newName = new PdfName(ns + sde.substring(7));
            desc.put(PdfName.BASEFONT, newName);
            ++total;
            PdfDictionary fd = desc.getAsDict(PdfName.FONTDESCRIPTOR);
            if (fd == null)
                continue;
            fd.put(PdfName.FONTNAME, newName);
        }
    }
    return total;
}
