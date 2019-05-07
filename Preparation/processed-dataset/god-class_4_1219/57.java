/*
     * (non-Javadoc)
     * @see megamek.common.Entity#getVibroClaws()
     */
@Override
public int getVibroClaws() {
    int claws = 0;
    for (Mounted mounted : getMisc()) {
        if (mounted.getType().hasFlag(MiscType.F_VIBROCLAW)) {
            claws++;
        }
    }
    return claws;
}
