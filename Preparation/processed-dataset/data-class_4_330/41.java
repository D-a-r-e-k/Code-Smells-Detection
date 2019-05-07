/**
     * Checks if this player is indian. This method returns the opposite of
     * {@link #isEuropean()}.
     *
     * @return <i>true</i> if this player is indian and <i>false</i>
     *         otherwise.
     */
public boolean isIndian() {
    return playerType == PlayerType.NATIVE;
}
