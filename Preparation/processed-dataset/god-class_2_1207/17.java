/**
     * Sets the interior gap and sends a {@link PlotChangeEvent} to all
     * registered listeners. This controls the space between the edges of the
     * plot and the plot area itself (the region where the axis labels appear).
     *
     * @param percent  the gap (as a percentage of the available drawing space).
     *
     * @see #getInteriorGap()
     */
public void setInteriorGap(double percent) {
    if ((percent < 0.0) || (percent > MAX_INTERIOR_GAP)) {
        throw new IllegalArgumentException("Percentage outside valid range.");
    }
    if (this.interiorGap != percent) {
        this.interiorGap = percent;
        fireChangeEvent();
    }
}
