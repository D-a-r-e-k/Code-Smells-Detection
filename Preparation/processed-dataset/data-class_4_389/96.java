/*
     * (non-Javadoc)
     * 
     * @see megamek.common.Entity#getTotalCommGearTons()
     */
@Override
public int getTotalCommGearTons() {
    return 1 + getExtraCommGearTons();
}
