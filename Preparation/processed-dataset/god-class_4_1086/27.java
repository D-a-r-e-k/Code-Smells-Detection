/**
     *
     */
public void releaseLastXrefPartial() {
    if (partial && lastXrefPartial != -1) {
        xrefObj.set(lastXrefPartial, null);
        lastXrefPartial = -1;
    }
}
