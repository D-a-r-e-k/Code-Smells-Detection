protected void readDocObjPartial() throws IOException {
    xrefObj = new ArrayList<PdfObject>(xref.length / 2);
    xrefObj.addAll(Collections.<PdfObject>nCopies(xref.length / 2, null));
    readDecryptedDocObj();
    if (objStmToOffset != null) {
        int keys[] = objStmToOffset.getKeys();
        for (int k = 0; k < keys.length; ++k) {
            int n = keys[k];
            objStmToOffset.put(n, xref[n * 2]);
            xref[n * 2] = -1;
        }
    }
}
