@SuppressWarnings("unchecked")
protected void removeUnusedNode(PdfObject obj, boolean hits[]) {
    Stack<Object> state = new Stack<Object>();
    state.push(obj);
    while (!state.empty()) {
        Object current = state.pop();
        if (current == null)
            continue;
        ArrayList<PdfObject> ar = null;
        PdfDictionary dic = null;
        PdfName[] keys = null;
        Object[] objs = null;
        int idx = 0;
        if (current instanceof PdfObject) {
            obj = (PdfObject) current;
            switch(obj.type()) {
                case PdfObject.DICTIONARY:
                case PdfObject.STREAM:
                    dic = (PdfDictionary) obj;
                    keys = new PdfName[dic.size()];
                    dic.getKeys().toArray(keys);
                    break;
                case PdfObject.ARRAY:
                    ar = ((PdfArray) obj).getArrayList();
                    break;
                case PdfObject.INDIRECT:
                    PRIndirectReference ref = (PRIndirectReference) obj;
                    int num = ref.getNumber();
                    if (!hits[num]) {
                        hits[num] = true;
                        state.push(getPdfObjectRelease(ref));
                    }
                    continue;
                default:
                    continue;
            }
        } else {
            objs = (Object[]) current;
            if (objs[0] instanceof ArrayList) {
                ar = (ArrayList<PdfObject>) objs[0];
                idx = ((Integer) objs[1]).intValue();
            } else {
                keys = (PdfName[]) objs[0];
                dic = (PdfDictionary) objs[1];
                idx = ((Integer) objs[2]).intValue();
            }
        }
        if (ar != null) {
            for (int k = idx; k < ar.size(); ++k) {
                PdfObject v = ar.get(k);
                if (v.isIndirect()) {
                    int num = ((PRIndirectReference) v).getNumber();
                    if (num >= xrefObj.size() || !partial && xrefObj.get(num) == null) {
                        ar.set(k, PdfNull.PDFNULL);
                        continue;
                    }
                }
                if (objs == null)
                    state.push(new Object[] { ar, new Integer(k + 1) });
                else {
                    objs[1] = new Integer(k + 1);
                    state.push(objs);
                }
                state.push(v);
                break;
            }
        } else {
            for (int k = idx; k < keys.length; ++k) {
                PdfName key = keys[k];
                PdfObject v = dic.get(key);
                if (v.isIndirect()) {
                    int num = ((PRIndirectReference) v).getNumber();
                    if (num >= xrefObj.size() || !partial && xrefObj.get(num) == null) {
                        dic.put(key, PdfNull.PDFNULL);
                        continue;
                    }
                }
                if (objs == null)
                    state.push(new Object[] { keys, dic, new Integer(k + 1) });
                else {
                    objs[2] = new Integer(k + 1);
                    state.push(objs);
                }
                state.push(v);
                break;
            }
        }
    }
}
