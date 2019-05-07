/**
     * Returns the <code>Colony</code> with the given name.
     *
     * @param name The name of the <code>Colony</code>.
     * @return The <code>Colony</code> or <code>null</code> if this player
     *         does not have a <code>Colony</code> with the specified name.
     */
public Colony getColony(String name) {
    for (Colony colony : getColonies()) {
        if (colony.getName().equals(name)) {
            return colony;
        }
    }
    return null;
}
