/**
     * Battle Armor units have no internals on their squad location.
     *
     * @see megamek.common.Infantry#getInternal( int )
     */
@Override
public int getInternal(int loc) {
    if (BattleArmor.LOC_SQUAD != loc) {
        return super.getInternal(loc);
    }
    return IArmorState.ARMOR_NA;
}
