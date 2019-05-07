/**
     * Setter for property appendable.
     * @param appendable New value of property appendable.
     */
public void setAppendable(boolean appendable) {
    this.appendable = appendable;
    if (appendable)
        getPdfObject(trailer.get(PdfName.ROOT));
}
