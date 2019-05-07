/**
     * Checks if this <code>Player</code> can get founding fathers.
     *
     * @return <code>true</code> if this player is european, not the royal
     *         expeditionary force and not currently fighting the war of
     *         independence.
     */
public boolean canHaveFoundingFathers() {
    return nationType.hasAbility("model.ability.electFoundingFather");
}
