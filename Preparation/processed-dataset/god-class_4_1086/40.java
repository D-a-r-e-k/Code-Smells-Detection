private void ensureXrefSize(int size) {
    if (size == 0)
        return;
    if (xref == null)
        xref = new int[size];
    else {
        if (xref.length < size) {
            int xref2[] = new int[size];
            System.arraycopy(xref, 0, xref2, 0, xref.length);
            xref = xref2;
        }
    }
}
