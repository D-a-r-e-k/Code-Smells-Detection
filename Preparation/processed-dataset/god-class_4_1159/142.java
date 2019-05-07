/**
     * Sets the number of immigration this player possess.
     *
     * @see #incrementImmigration(int)
     */
public void reduceImmigration() {
    if (!canRecruitUnits()) {
        return;
    }
    int cost = getSpecification().getBoolean(GameOptions.SAVE_PRODUCTION_OVERFLOW) ? immigrationRequired : immigration;
    if (cost > immigration) {
        immigration = 0;
    } else {
        immigration -= cost;
    }
}
