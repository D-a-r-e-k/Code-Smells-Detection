/**
     * Returns this entity's walking mp, factored for extreme temperatures and
     * gravity.
     */
@Override
public int getWalkMP(boolean gravity, boolean ignoreheat) {
    int j = getOriginalWalkMP();
    if (null != game) {
        int weatherMod = game.getPlanetaryConditions().getMovementMods(this);
        if (weatherMod != 0) {
            j = Math.max(j + weatherMod, 0);
        }
    }
    if (gravity) {
        j = applyGravityEffectsOnMP(j);
    }
    return j;
}
