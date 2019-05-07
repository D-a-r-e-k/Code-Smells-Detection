/**
     * Checks if this <code>Player</code> can recruit units by producing
     * immigration.
     *
     * @return <code>true</code> if units can be recruited by this
     *         <code>Player</code>.
     */
public boolean canRecruitUnits() {
    return playerType == PlayerType.COLONIAL;
}
