/**
     * Sets the URL generator for the plot and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getURLGenerator()
     *
     * @since 1.0.2
     */
public void setURLGenerator(CategoryURLGenerator generator) {
    this.urlGenerator = generator;
    fireChangeEvent();
}
