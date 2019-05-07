/**
     * Modifies the hostility against the given player.
     *
     * @param player The <code>Player</code>.
     * @param addToTension The amount to add to the current tension level.
     * @param origin A <code>Settlement</code> where the alarming event
     *     occurred.
     * @return A list of objects that may need updating due to the tension
     *     change (such as native settlements).
     */
public List<FreeColGameObject> modifyTension(Player player, int addToTension, Settlement origin) {
    if (player == null) {
        throw new IllegalStateException("Null player");
    } else if (player == this) {
        throw new IllegalStateException("Self tension!");
    } else if (origin != null && origin.getOwner() != this) {
        throw new IllegalStateException("Bogus origin:" + origin.getId());
    }
    List<FreeColGameObject> objects = new ArrayList<FreeColGameObject>();
    Tension.Level oldLevel = getTension(player).getLevel();
    getTension(player).modify(addToTension);
    if (oldLevel != getTension(player).getLevel()) {
        objects.add(this);
    }
    // Propagate tension change as settlement alarm to all 
    // settlements except the one that originated it (if any). 
    for (Settlement settlement : settlements) {
        if (!settlement.equals(origin)) {
            if (settlement.propagateAlarm(player, addToTension)) {
                objects.add(settlement);
            }
        }
    }
    return objects;
}
