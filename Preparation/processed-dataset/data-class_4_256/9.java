static void findAllObjects(PdfReader reader, PdfObject obj, IntHashtable hits) {
    if (obj == null)
        return;
    switch(obj.type()) {
        case PdfObject.INDIRECT:
            PRIndirectReference iref = (PRIndirectReference) obj;
            if (reader != iref.getReader())
                return;
            if (hits.containsKey(iref.getNumber()))
                return;
            hits.put(iref.getNumber(), 1);
            findAllObjects(reader, PdfReader.getPdfObject(obj), hits);
            return;
        case PdfObject.ARRAY:
            PdfArray a = (PdfArray) obj;
            for (int k = 0; k < a.size(); ++k) {
                findAllObjects(reader, a.getPdfObject(k), hits);
            }
            return;
        case PdfObject.DICTIONARY:
        case PdfObject.STREAM:
            PdfDictionary dic = (PdfDictionary) obj;
            for (Object element : dic.getKeys()) {
                PdfName name = (PdfName) element;
                findAllObjects(reader, dic.get(name), hits);
            }
            return;
    }
}
