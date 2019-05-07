/**
     * Checks if this player is european. This includes the "Royal Expeditionay
     * Force".
     *
     * @return <i>true</i> if this player is european and <i>false</i>
     *         otherwise.
     */
public boolean isEuropean() {
    return nationType != null && nationType.isEuropean();
}
