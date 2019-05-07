/**
     * Sets the webFilled flag and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param flag  the flag.
     *
     * @see #isWebFilled()
     */
public void setWebFilled(boolean flag) {
    this.webFilled = flag;
    fireChangeEvent();
}
