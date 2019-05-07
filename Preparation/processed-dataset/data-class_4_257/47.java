void addFileAttachment(String description, PdfFileSpecification fs) throws IOException {
    if (description == null) {
        PdfString desc = (PdfString) fs.get(PdfName.DESC);
        if (desc == null) {
            description = "";
        } else {
            description = PdfEncodings.convertToString(desc.getBytes(), null);
        }
    }
    fs.addDescription(description, true);
    if (description.length() == 0)
        description = "Unnamed";
    String fn = PdfEncodings.convertToString(new PdfString(description, PdfObject.TEXT_UNICODE).getBytes(), null);
    int k = 0;
    while (documentFileAttachment.containsKey(fn)) {
        ++k;
        fn = PdfEncodings.convertToString(new PdfString(description + " " + k, PdfObject.TEXT_UNICODE).getBytes(), null);
    }
    documentFileAttachment.put(fn, fs.getReference());
}
