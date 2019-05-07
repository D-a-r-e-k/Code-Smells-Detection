/**
     * Battle Armor units have no armor on their squad location.
     *
     * @see megamek.common.Infantry#getOArmor( int, boolean )
     */
@Override
public int getOArmor(int loc, boolean rear) {
    if (BattleArmor.LOC_SQUAD != loc) {
        return super.getOArmor(loc, rear);
    }
    return IArmorState.ARMOR_NA;
}
