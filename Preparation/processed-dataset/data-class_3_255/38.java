/**
     * Sets the run direction.
     *
     * @param runDirection the run direction
     */
public void setRunDirection(int runDirection) {
    if (runDirection < PdfWriter.RUN_DIRECTION_DEFAULT || runDirection > PdfWriter.RUN_DIRECTION_RTL)
        throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
    this.runDirection = runDirection;
}
