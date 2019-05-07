/**
     * Checks if this <code>Player</code> can build colonies.
     *
     * @return <code>true</code> if this player is european, not the royal
     *         expeditionary force and not currently fighting the war of
     *         independence.
     */
public boolean canBuildColonies() {
    return nationType.hasAbility("model.ability.foundColony");
}
