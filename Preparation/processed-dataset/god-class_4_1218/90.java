/**
     * Tanks can't spot when stunned.
     */
@Override
public boolean canSpot() {
    return super.canSpot() && (getStunnedTurns() == 0);
}
