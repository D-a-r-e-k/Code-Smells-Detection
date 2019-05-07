/**
     * Sets the rearrangeTurn variable such that rearrangeWorkers will
     * run fully next time it is invoked.
     */
public void requestRearrange() {
    rearrangeTurn = new Turn(0);
}
