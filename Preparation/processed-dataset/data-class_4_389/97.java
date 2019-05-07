/*
     * (non-Javadoc)
     * 
     * @see megamek.common.Entity#getIniBonus()
     */
@Override
public int getHQIniBonus() {
    int bonus = super.getHQIniBonus();
    if (((stabiliserHits > 0) && (mpUsedLastRound > 0)) || commanderHit) {
        return 0;
    }
    return bonus;
}
