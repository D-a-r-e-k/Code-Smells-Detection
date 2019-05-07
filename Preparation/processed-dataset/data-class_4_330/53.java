/**
     * Checks if this player has work to do if it is a REF-player.
     *
     * @return True if any of our units are located in the new
     *     world or a nation is in rebellion against us.
     */
public boolean isWorkForREF() {
    for (Unit u : getUnits()) {
        // Work to do if unit in the new world 
        if (u.getTile() != null)
            return true;
    }
    return !getRebels().isEmpty();
}
