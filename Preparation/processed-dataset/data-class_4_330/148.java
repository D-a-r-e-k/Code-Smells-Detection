/**
     * Gets the number of immigration required to cause a new colonist to emigrate.
     *
     * @return The number of immigration required to cause a new colonist to
     *         emigrate.
     */
public int getImmigrationRequired() {
    return (canRecruitUnits()) ? immigrationRequired : 0;
}
