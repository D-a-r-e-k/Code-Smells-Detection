/**
     * Adds a <CODE>Chunk</CODE> to the current text array.
     * Will not have any effect if addElement() was called before.
     *
     * @param chunk the text
     */
public void addText(Chunk chunk) {
    if (chunk == null || composite)
        return;
    addText(new Phrase(chunk));
}
