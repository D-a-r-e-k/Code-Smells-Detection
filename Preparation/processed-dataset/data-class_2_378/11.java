/**
     * Sets the starting angle and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * <P>
     * The initial default value is 90 degrees, which corresponds to 12 o'clock.
     * A value of zero corresponds to 3 o'clock... this is the encoding used by
     * Java's Arc2D class.
     *
     * @param angle  the angle (in degrees).
     *
     * @see #getStartAngle()
     */
public void setStartAngle(double angle) {
    this.startAngle = angle;
    fireChangeEvent();
}
