private boolean replaceNamedDestination(PdfObject obj, HashMap<Object, PdfObject> names) {
    obj = getPdfObject(obj);
    int objIdx = lastXrefPartial;
    releaseLastXrefPartial();
    if (obj != null && obj.isDictionary()) {
        PdfObject ob2 = getPdfObjectRelease(((PdfDictionary) obj).get(PdfName.DEST));
        Object name = null;
        if (ob2 != null) {
            if (ob2.isName())
                name = ob2;
            else if (ob2.isString())
                name = ob2.toString();
            PdfArray dest = (PdfArray) names.get(name);
            if (dest != null) {
                ((PdfDictionary) obj).put(PdfName.DEST, dest);
                setXrefPartialObject(objIdx, obj);
                return true;
            }
        } else if ((ob2 = getPdfObject(((PdfDictionary) obj).get(PdfName.A))) != null) {
            int obj2Idx = lastXrefPartial;
            releaseLastXrefPartial();
            PdfDictionary dic = (PdfDictionary) ob2;
            PdfName type = (PdfName) getPdfObjectRelease(dic.get(PdfName.S));
            if (PdfName.GOTO.equals(type)) {
                PdfObject ob3 = getPdfObjectRelease(dic.get(PdfName.D));
                if (ob3 != null) {
                    if (ob3.isName())
                        name = ob3;
                    else if (ob3.isString())
                        name = ob3.toString();
                }
                PdfArray dest = (PdfArray) names.get(name);
                if (dest != null) {
                    dic.put(PdfName.D, dest);
                    setXrefPartialObject(obj2Idx, ob2);
                    setXrefPartialObject(objIdx, obj);
                    return true;
                }
            }
        }
    }
    return false;
}
