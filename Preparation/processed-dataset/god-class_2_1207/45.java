/**
     * Sets the series label font and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param font  the font (<code>null</code> not permitted).
     *
     * @see #getLabelFont()
     */
public void setLabelFont(Font font) {
    if (font == null) {
        throw new IllegalArgumentException("Null 'font' argument.");
    }
    this.labelFont = font;
    fireChangeEvent();
}
