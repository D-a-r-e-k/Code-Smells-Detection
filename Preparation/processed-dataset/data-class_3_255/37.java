/**
     * Sets the ratio between the extra word spacing and the extra character
     * spacing when the text is fully justified.
     * Extra word spacing will grow <CODE>spaceCharRatio</CODE> times more
     * than extra character spacing.
     * If the ratio is <CODE>PdfWriter.NO_SPACE_CHAR_RATIO</CODE> then the
     * extra character spacing will be zero.
     *
     * @param spaceCharRatio the ratio between the extra word spacing and the extra character spacing
     */
public void setSpaceCharRatio(float spaceCharRatio) {
    this.spaceCharRatio = spaceCharRatio;
}
