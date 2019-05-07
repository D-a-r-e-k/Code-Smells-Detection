/**
     * Sets the leading fixed and variable. The resultant leading will be
     * fixedLeading+multipliedLeading*maxFontSize where maxFontSize is the
     * size of the biggest font in the line.
     *
     * @param fixedLeading the fixed leading
     * @param multipliedLeading the variable leading
     */
public void setLeading(float fixedLeading, float multipliedLeading) {
    this.fixedLeading = fixedLeading;
    this.multipliedLeading = multipliedLeading;
}
