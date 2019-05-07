/**
     * Sets the nation for this player.
     *
     * @param newNation The new nation for this player.
     */
public void setNation(Nation newNation) {
    Nation oldNation = getNation();
    nationID = newNation.getId();
    getGame().getNationOptions().getNations().put(newNation, NationState.NOT_AVAILABLE);
    getGame().getNationOptions().getNations().put(oldNation, NationState.AVAILABLE);
}
