@Override
protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
    IntHashtable ref = readers2intrefs.get(reader);
    if (ref != null) {
        int n = ref.get(number);
        if (n == 0) {
            n = getIndirectReferenceNumber();
            ref.put(number, n);
        }
        return n;
    }
    if (currentPdfReaderInstance == null) {
        if (append && number < initialXrefSize)
            return number;
        int n = myXref.get(number);
        if (n == 0) {
            n = getIndirectReferenceNumber();
            myXref.put(number, n);
        }
        return n;
    } else
        return currentPdfReaderInstance.getNewObjectNumber(number, generation);
}
