/**
     * Sets the maximum value any category axis can take and sends
     * a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param value  the maximum value.
     *
     * @see #getMaxValue()
     */
public void setMaxValue(double value) {
    this.maxValue = value;
    fireChangeEvent();
}
