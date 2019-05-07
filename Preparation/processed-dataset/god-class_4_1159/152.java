/**
     * Modifies the hostility against the given player.
     *
     * @param player The <code>Player</code>.
     * @param addToTension The amount to add to the current tension level.
     * @return A list of objects that may need updating due to the tension
     *     change (such as native settlements).
     */
public List<FreeColGameObject> modifyTension(Player player, int addToTension) {
    return modifyTension(player, addToTension, null);
}
