/**
     * Returns true if this weapon fires into the secondary facing arc. If
     * false, assume it fires into the primary.
     */
@Override
public boolean isSecondaryArcWeapon(int weaponId) {
    if (getEquipment(weaponId).getLocation() == LOC_TURRET) {
        return true;
    }
    return false;
}
