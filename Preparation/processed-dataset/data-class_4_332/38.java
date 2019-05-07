/**
     * Checks the integrity of a this AIColony
     *
     * @return True if the colony is intact.
     */
public boolean checkIntegrity() {
    return super.checkIntegrity() && colony != null && !colony.isDisposed();
}
