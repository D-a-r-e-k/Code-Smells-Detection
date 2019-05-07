/**
     * Battle Armor units have no internals on their squad location.
     *
     * @see megamek.common.Infantry#getOInternal( int )
     */
@Override
public int getOInternal(int loc) {
    if (BattleArmor.LOC_SQUAD != loc) {
        return super.getOInternal(loc);
    }
    return IArmorState.ARMOR_NA;
}
