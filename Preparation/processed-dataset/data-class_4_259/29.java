private void setXrefPartialObject(int idx, PdfObject obj) {
    if (!partial || idx < 0)
        return;
    xrefObj.set(idx, obj);
}
