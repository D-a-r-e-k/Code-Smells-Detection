/**
     * Adds a <CODE>Phrase</CODE> to the current text array.
     * Will not have any effect if addElement() was called before.
     *
     * @param phrase the text
     */
public void addText(Phrase phrase) {
    if (phrase == null || composite)
        return;
    addWaitingPhrase();
    if (bidiLine == null) {
        waitPhrase = phrase;
        return;
    }
    for (Object element : phrase.getChunks()) {
        bidiLine.addChunk(new PdfChunk((Chunk) element, null));
    }
}
