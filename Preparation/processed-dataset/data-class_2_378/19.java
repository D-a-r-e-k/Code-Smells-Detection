/**
     * Sets the axis label gap and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param gap  the gap.
     *
     * @see #getAxisLabelGap()
     */
public void setAxisLabelGap(double gap) {
    this.axisLabelGap = gap;
    fireChangeEvent();
}
