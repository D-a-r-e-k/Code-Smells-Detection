/**
     * Battle Armor units have as many critical slots as they need to hold their
     * equipment.
     */
@Override
protected int[] getNoOfSlots() {
    if (!isInitialized || isClan()) {
        return CLAN_NUM_OF_SLOTS;
    }
    return IS_NUM_OF_SLOTS;
}
