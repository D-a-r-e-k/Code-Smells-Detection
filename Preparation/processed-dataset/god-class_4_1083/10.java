/**
     * @param fdf
     * @throws IOException
     */
public void addComments(FdfReader fdf) throws IOException {
    if (readers2intrefs.containsKey(fdf))
        return;
    PdfDictionary catalog = fdf.getCatalog();
    catalog = catalog.getAsDict(PdfName.FDF);
    if (catalog == null)
        return;
    PdfArray annots = catalog.getAsArray(PdfName.ANNOTS);
    if (annots == null || annots.size() == 0)
        return;
    registerReader(fdf, false);
    IntHashtable hits = new IntHashtable();
    HashMap<String, PdfObject> irt = new HashMap<String, PdfObject>();
    ArrayList<PdfObject> an = new ArrayList<PdfObject>();
    for (int k = 0; k < annots.size(); ++k) {
        PdfObject obj = annots.getPdfObject(k);
        PdfDictionary annot = (PdfDictionary) PdfReader.getPdfObject(obj);
        PdfNumber page = annot.getAsNumber(PdfName.PAGE);
        if (page == null || page.intValue() >= reader.getNumberOfPages())
            continue;
        findAllObjects(fdf, obj, hits);
        an.add(obj);
        if (obj.type() == PdfObject.INDIRECT) {
            PdfObject nm = PdfReader.getPdfObject(annot.get(PdfName.NM));
            if (nm != null && nm.type() == PdfObject.STRING)
                irt.put(nm.toString(), obj);
        }
    }
    int arhits[] = hits.getKeys();
    for (int k = 0; k < arhits.length; ++k) {
        int n = arhits[k];
        PdfObject obj = fdf.getPdfObject(n);
        if (obj.type() == PdfObject.DICTIONARY) {
            PdfObject str = PdfReader.getPdfObject(((PdfDictionary) obj).get(PdfName.IRT));
            if (str != null && str.type() == PdfObject.STRING) {
                PdfObject i = irt.get(str.toString());
                if (i != null) {
                    PdfDictionary dic2 = new PdfDictionary();
                    dic2.merge((PdfDictionary) obj);
                    dic2.put(PdfName.IRT, i);
                    obj = dic2;
                }
            }
        }
        addToBody(obj, getNewObjectNumber(fdf, n, 0));
    }
    for (int k = 0; k < an.size(); ++k) {
        PdfObject obj = an.get(k);
        PdfDictionary annot = (PdfDictionary) PdfReader.getPdfObject(obj);
        PdfNumber page = annot.getAsNumber(PdfName.PAGE);
        PdfDictionary dic = reader.getPageN(page.intValue() + 1);
        PdfArray annotsp = (PdfArray) PdfReader.getPdfObject(dic.get(PdfName.ANNOTS), dic);
        if (annotsp == null) {
            annotsp = new PdfArray();
            dic.put(PdfName.ANNOTS, annotsp);
            markUsed(dic);
        }
        markUsed(annotsp);
        annotsp.add(obj);
    }
}
