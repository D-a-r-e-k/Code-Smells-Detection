/**
     * Sets the nation type of this player.
     *
     * @param newNationType a <code>NationType</code> value
     */
public void setNationType(NationType newNationType) {
    if (nationType != null)
        removeFeatures(nationType);
    nationType = newNationType;
    if (newNationType != null)
        addFeatures(newNationType);
}
