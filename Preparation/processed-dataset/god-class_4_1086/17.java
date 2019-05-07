protected void readPdfPartial() throws IOException {
    try {
        fileLength = tokens.getFile().length();
        pdfVersion = tokens.checkPdfHeader();
        try {
            readXref();
        } catch (Exception e) {
            try {
                rebuilt = true;
                rebuildXref();
                lastXref = -1;
            } catch (Exception ne) {
                throw new InvalidPdfException(MessageLocalization.getComposedMessage("rebuild.failed.1.original.message.2", ne.getMessage(), e.getMessage()));
            }
        }
        readDocObjPartial();
        readPages();
    } catch (IOException e) {
        try {
            tokens.close();
        } catch (Exception ee) {
        }
        throw e;
    }
}
