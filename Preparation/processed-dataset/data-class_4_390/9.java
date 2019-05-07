/**
     * get this BA's jump MP, possibly ignoring gravity and burden
     * @param gravity
     * @param ignoreBurden
     * @return
     */
public int getJumpMP(boolean gravity, boolean ignoreBurden) {
    if (isBurdened() && !ignoreBurden) {
        return 0;
    }
    if (null != game) {
        int windCond = game.getPlanetaryConditions().getWindStrength();
        if (windCond >= PlanetaryConditions.WI_STORM) {
            return 0;
        }
    }
    int mp = getOriginalJumpMP();
    if (getMovementMode() == EntityMovementMode.INF_UMU) {
        mp = 0;
    }
    if (gravity) {
        mp = applyGravityEffectsOnMP(mp);
    }
    return mp;
}
