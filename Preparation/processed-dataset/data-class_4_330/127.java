/**
     * Gets the name to display for this player.
     * TODO: This is a kludge that should be fixed.
     *
     * @return The name to display for this player.
     */
public String getDisplayName() {
    return (getName().startsWith("model.nation.")) ? Messages.message(getName()) : getName();
}
