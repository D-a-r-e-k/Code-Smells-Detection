/**
     * Sets the stance towards a given player to one of
     * WAR, CEASE_FIRE, PEACE and ALLIANCE.
     *
     * @param player The <code>Player</code>.
     * @param newStance The new <code>Stance</code>.
     * @return True if the stance change was valid.
     * @throws IllegalArgumentException if player is null or this.
     */
public boolean setStance(Player player, Stance newStance) {
    if (player == null) {
        throw new IllegalArgumentException("Player must not be 'null'.");
    }
    if (player == this) {
        throw new IllegalArgumentException("Cannot set the stance towards ourselves.");
    }
    if (newStance == null) {
        stance.remove(player.getId());
        return true;
    }
    Stance oldStance = stance.get(player.getId());
    if (newStance.equals(oldStance))
        return true;
    boolean valid = true;
    ;
    if ((newStance == Stance.CEASE_FIRE && oldStance != Stance.WAR) || newStance == Stance.UNCONTACTED) {
        valid = false;
    }
    stance.put(player.getId(), newStance);
    return valid;
}
