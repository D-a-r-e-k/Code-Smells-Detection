/**
     * Converts a remote named destination GoToR with a local named destination
     * if there's a corresponding name.
     * @param	obj	an annotation that needs to be screened for links to external named destinations.
     * @param	names	a map with names of local named destinations
     * @since	iText 5.0
     */
private boolean convertNamedDestination(PdfObject obj, HashMap<Object, PdfObject> names) {
    obj = getPdfObject(obj);
    int objIdx = lastXrefPartial;
    releaseLastXrefPartial();
    if (obj != null && obj.isDictionary()) {
        PdfObject ob2 = getPdfObject(((PdfDictionary) obj).get(PdfName.A));
        if (ob2 != null) {
            int obj2Idx = lastXrefPartial;
            releaseLastXrefPartial();
            PdfDictionary dic = (PdfDictionary) ob2;
            PdfName type = (PdfName) getPdfObjectRelease(dic.get(PdfName.S));
            if (PdfName.GOTOR.equals(type)) {
                PdfObject ob3 = getPdfObjectRelease(dic.get(PdfName.D));
                Object name = null;
                if (ob3 != null) {
                    if (ob3.isName())
                        name = ob3;
                    else if (ob3.isString())
                        name = ob3.toString();
                    PdfArray dest = (PdfArray) names.get(name);
                    if (dest != null) {
                        dic.remove(PdfName.F);
                        dic.remove(PdfName.NEWWINDOW);
                        dic.put(PdfName.S, PdfName.GOTO);
                        setXrefPartialObject(obj2Idx, ob2);
                        setXrefPartialObject(objIdx, obj);
                        return true;
                    }
                }
            }
        }
    }
    return false;
}
