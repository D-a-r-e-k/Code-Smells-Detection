/**
     * Returns this entity's running mp, factored for extreme temperatures and
     * gravity.
     */
@Override
public int getRunMP(boolean gravity, boolean ignoreheat) {
    if (getMovementMode() == EntityMovementMode.INF_UMU) {
        return getOriginalJumpMP();
    }
    return getWalkMP(gravity, ignoreheat);
}
