/**
     * Returns this entity's walking/cruising mp, factored for heat, extreme
     * temperatures, and gravity.
     */
@Override
public int getWalkMP(boolean gravity, boolean ignoreheat) {
    int j = getOriginalWalkMP();
    j = Math.max(0, j - getCargoMpReduction());
    if (null != game) {
        int weatherMod = game.getPlanetaryConditions().getMovementMods(this);
        if (weatherMod != 0) {
            j = Math.max(j + weatherMod, 0);
        }
    }
    if (hasModularArmor()) {
        j--;
    }
    if (gravity) {
        j = applyGravityEffectsOnMP(j);
    }
    return j;
}
