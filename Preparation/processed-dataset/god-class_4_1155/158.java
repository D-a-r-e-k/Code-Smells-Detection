/**
     * Confirms that a unit should move somewhere where it would have
     * to abandon its participation in education (if any).
     *
     * @param unit The <code>Unit</code> to check.
     * @param checkStudent Should we check for student movements.
     * @return True if the unit should proceed to move.
     */
public boolean confirmAbandonEducation(Unit unit, boolean checkStudent) {
    StringTemplate message = unit.getAbandonEducationMessage(checkStudent);
    return message == null || showConfirmDialog(unit.getTile(), message, "abandonEducation.yes", "abandonEducation.no");
}
