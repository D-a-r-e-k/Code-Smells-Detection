private void addWaitingPhrase() {
    if (bidiLine == null && waitPhrase != null) {
        bidiLine = new BidiLine();
        for (Chunk c : waitPhrase.getChunks()) {
            bidiLine.addChunk(new PdfChunk(c, null));
        }
        waitPhrase = null;
    }
}
