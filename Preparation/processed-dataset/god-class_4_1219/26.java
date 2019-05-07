/**
     * Calculates the battle value of this platoon.
     */
@Override
public int calculateBattleValue(boolean ignoreC3, boolean ignorePilot) {
    return calculateBattleValue(ignoreC3, ignorePilot, false);
}
