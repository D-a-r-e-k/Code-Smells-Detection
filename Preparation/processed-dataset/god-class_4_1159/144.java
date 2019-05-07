/**
     * Sets the number of immigration this player possess.
     *
     * @param immigration The immigration value for this player.
     */
public void setImmigration(int immigration) {
    if (canRecruitUnits()) {
        this.immigration = immigration;
    }
}
