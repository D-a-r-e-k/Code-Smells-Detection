/**
     * Sets the first line adjustment.
     * Some objects have properties, like spacing before, that behave
     * differently if the object is the first to be written after go() or not.
     * The first line adjustment is <CODE>true</CODE> by default but can be
     * changed if several objects are to be placed one after the other in the
     * same column calling go() several times.
     *
     * @param adjustFirstLine <CODE>true</CODE> to adjust the first line, <CODE>false</CODE> otherwise
     */
public void setAdjustFirstLine(boolean adjustFirstLine) {
    this.adjustFirstLine = adjustFirstLine;
}
