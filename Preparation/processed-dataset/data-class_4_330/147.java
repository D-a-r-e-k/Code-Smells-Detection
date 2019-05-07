/**
     * Checks to see whether or not a colonist can emigrate, and does so if
     * possible.
     *
     * @return Whether a new colonist should immigrate.
     */
public boolean checkEmigrate() {
    if (!canRecruitUnits()) {
        return false;
    }
    return getImmigrationRequired() <= immigration;
}
