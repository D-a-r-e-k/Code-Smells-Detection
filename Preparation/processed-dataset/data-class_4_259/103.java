protected static PdfObject duplicatePdfObject(PdfObject original, PdfReader newReader) {
    if (original == null)
        return null;
    switch(original.type()) {
        case PdfObject.DICTIONARY:
            {
                return duplicatePdfDictionary((PdfDictionary) original, null, newReader);
            }
        case PdfObject.STREAM:
            {
                PRStream org = (PRStream) original;
                PRStream stream = new PRStream(org, null, newReader);
                duplicatePdfDictionary(org, stream, newReader);
                return stream;
            }
        case PdfObject.ARRAY:
            {
                PdfArray arr = new PdfArray();
                for (Iterator<PdfObject> it = ((PdfArray) original).listIterator(); it.hasNext(); ) {
                    arr.add(duplicatePdfObject(it.next(), newReader));
                }
                return arr;
            }
        case PdfObject.INDIRECT:
            {
                PRIndirectReference org = (PRIndirectReference) original;
                return new PRIndirectReference(newReader, org.getNumber(), org.getGeneration());
            }
        default:
            return original;
    }
}
