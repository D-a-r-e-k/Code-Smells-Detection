/**
     * Try to remove a unit from a colony.
     * - Check for population limit.
     * - Query if education should be abandoned.
     *
     * @param unit The <code>Unit</code> that is leaving the colony.
     * @return True if the unit is allowed to leave.
     */
public boolean tryLeaveColony(Unit unit) {
    Colony colony = unit.getColony();
    String message = colony.getReducePopulationMessage();
    if (message != null) {
        showInformationMessage(message);
        return false;
    }
    return confirmAbandonEducation(unit, true);
}
