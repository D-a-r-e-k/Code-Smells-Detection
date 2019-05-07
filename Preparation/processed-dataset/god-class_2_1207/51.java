/**
     * Sets the tool tip generator for the plot and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getToolTipGenerator()
     *
     * @since 1.0.2
     */
public void setToolTipGenerator(CategoryToolTipGenerator generator) {
    this.toolTipGenerator = generator;
    fireChangeEvent();
}
