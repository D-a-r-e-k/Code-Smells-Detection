/**
     * The the number of milliseconds by which a trigger must have missed its
     * next-fire-time, in order for it to be considered "misfired" and thus
     * have its misfire instruction applied.
     * 
     * @param misfireThreshold
     */
public void setMisfireThreshold(long misfireThreshold) {
    if (misfireThreshold < 1) {
        throw new IllegalArgumentException("Misfirethreshold must be larger than 0");
    }
    this.misfireThreshold = misfireThreshold;
}
