void addFileAttachments() throws IOException {
    HashMap<String, PdfObject> fs = pdf.getDocumentFileAttachment();
    if (fs.isEmpty())
        return;
    PdfDictionary catalog = reader.getCatalog();
    PdfDictionary names = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES), catalog);
    if (names == null) {
        names = new PdfDictionary();
        catalog.put(PdfName.NAMES, names);
        markUsed(catalog);
    }
    markUsed(names);
    HashMap<String, PdfObject> old = PdfNameTree.readTree((PdfDictionary) PdfReader.getPdfObjectRelease(names.get(PdfName.EMBEDDEDFILES)));
    for (Map.Entry<String, PdfObject> entry : fs.entrySet()) {
        String name = entry.getKey();
        int k = 0;
        String nn = name;
        while (old.containsKey(nn)) {
            ++k;
            nn += " " + k;
        }
        old.put(nn, entry.getValue());
    }
    PdfDictionary tree = PdfNameTree.writeTree(old, this);
    // Remove old EmbeddedFiles object if preset 
    PdfObject oldEmbeddedFiles = names.get(PdfName.EMBEDDEDFILES);
    if (oldEmbeddedFiles != null) {
        PdfReader.killIndirect(oldEmbeddedFiles);
    }
    // Add new EmbeddedFiles object 
    names.put(PdfName.EMBEDDEDFILES, addToBody(tree).getIndirectReference());
}
