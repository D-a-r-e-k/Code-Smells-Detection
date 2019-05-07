/**
     * Gets the number of immigration this player possess.
     *
     * @return The number.
     * @see #reduceImmigration
     */
public int getImmigration() {
    return (canRecruitUnits()) ? immigration : 0;
}
