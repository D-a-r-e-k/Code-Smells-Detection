/**
     * Battle Armor units have no armor on their squad location.
     *
     * @see megamek.common.Infantry#getArmor( int, boolean )
     */
@Override
public int getArmor(int loc, boolean rear) {
    if (BattleArmor.LOC_SQUAD != loc) {
        return super.getArmor(loc, rear);
    }
    return IArmorState.ARMOR_NA;
}
