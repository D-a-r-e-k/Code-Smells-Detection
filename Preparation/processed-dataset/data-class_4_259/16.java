protected void readPdf() throws IOException {
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
        try {
            readDocObj();
        } catch (Exception e) {
            if (e instanceof BadPasswordException)
                throw new BadPasswordException(e.getMessage());
            if (rebuilt || encryptionError)
                throw new InvalidPdfException(e.getMessage());
            rebuilt = true;
            encrypted = false;
            rebuildXref();
            lastXref = -1;
            readDocObj();
        }
        strings.clear();
        readPages();
        eliminateSharedStreams();
        removeUnusedObjects();
    } finally {
        try {
            tokens.close();
        } catch (Exception e) {
        }
    }
}
