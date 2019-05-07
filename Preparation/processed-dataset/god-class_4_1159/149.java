/**
     * Sets the number of immigration required to cause a new colonist to emigrate.
     *
     * @param immigrationRequired The number of immigration required to cause a new
     *            colonist to emigrate.
     */
public void setImmigrationRequired(int immigrationRequired) {
    if (canRecruitUnits()) {
        this.immigrationRequired = immigrationRequired;
    }
}
