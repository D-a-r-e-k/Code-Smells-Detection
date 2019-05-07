/**
     * Sets the label generator and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param generator  the generator (<code>null</code> not permitted).
     *
     * @see #getLabelGenerator()
     */
public void setLabelGenerator(CategoryItemLabelGenerator generator) {
    if (generator == null) {
        throw new IllegalArgumentException("Null 'generator' argument.");
    }
    this.labelGenerator = generator;
}
