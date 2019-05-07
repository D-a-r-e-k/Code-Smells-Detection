/**
     * Outputs the lines to the document. It is equivalent to <CODE>go(false)</CODE>.
     *
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     * @throws DocumentException on error
     */
public int go() throws DocumentException {
    return go(false);
}
