/**
     * Sets the head percent and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param percent  the percent.
     *
     * @see #getHeadPercent()
     */
public void setHeadPercent(double percent) {
    this.headPercent = percent;
    fireChangeEvent();
}
