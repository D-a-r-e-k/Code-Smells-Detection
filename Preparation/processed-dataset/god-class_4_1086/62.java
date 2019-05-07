protected void killXref(PdfObject obj) {
    if (obj == null)
        return;
    if (obj instanceof PdfIndirectReference && !obj.isIndirect())
        return;
    switch(obj.type()) {
        case PdfObject.INDIRECT:
            {
                int xr = ((PRIndirectReference) obj).getNumber();
                obj = xrefObj.get(xr);
                xrefObj.set(xr, null);
                freeXref = xr;
                killXref(obj);
                break;
            }
        case PdfObject.ARRAY:
            {
                PdfArray t = (PdfArray) obj;
                for (int i = 0; i < t.size(); ++i) killXref(t.getPdfObject(i));
                break;
            }
        case PdfObject.STREAM:
        case PdfObject.DICTIONARY:
            {
                PdfDictionary dic = (PdfDictionary) obj;
                for (Object element : dic.getKeys()) {
                    killXref(dic.get((PdfName) element));
                }
                break;
            }
    }
}
