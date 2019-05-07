/**
     * @param aMinimumBandwidth The minimumBandwidth to set.
     */
public void setMinimumBandwidth(int aMinimumBandwidth) {
    if (aMinimumBandwidth <= 0) {
        aMinimumBandwidth = 1;
    }
    this.minimumBandwidth = aMinimumBandwidth;
}
