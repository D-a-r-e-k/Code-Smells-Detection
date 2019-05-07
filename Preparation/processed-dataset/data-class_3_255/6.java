/**
     * Replaces the current text array with this <CODE>Phrase</CODE>.
     * Anything added previously with addElement() is lost.
     *
     * @param phrase the text
     */
public void setText(Phrase phrase) {
    bidiLine = null;
    composite = false;
    compositeColumn = null;
    compositeElements = null;
    listIdx = 0;
    splittedRow = false;
    waitPhrase = phrase;
}
