/**
     * return if this BA has fire resistant armor
     * @return
     */
public boolean isFireResistant() {
    // when BA is created from custom BA dialog  
    if (armorType == 7) {
        return true;
    } else if (armorType == -1) {
        for (Mounted equip : getMisc()) {
            if (equip.getType().hasFlag(MiscType.F_FIRE_RESISTANT)) {
                return true;
            }
        }
    }
    return false;
}
