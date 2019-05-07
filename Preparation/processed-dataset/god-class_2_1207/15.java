/**
     * Sets the direction in which the radar axes are drawn and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param direction  the direction (<code>null</code> not permitted).
     *
     * @see #getDirection()
     */
public void setDirection(Rotation direction) {
    if (direction == null) {
        throw new IllegalArgumentException("Null 'direction' argument.");
    }
    this.direction = direction;
    fireChangeEvent();
}
