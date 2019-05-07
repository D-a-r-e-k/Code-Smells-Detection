/**
     * apply the effects of an "engine hit" crit
     */
public void engineHit() {
    engineHit = true;
    immobilize();
    lockTurret();
    for (Mounted m : getWeaponList()) {
        WeaponType wtype = (WeaponType) m.getType();
        if (wtype.hasFlag(WeaponType.F_ENERGY)) {
            m.setBreached(true);
        }
    }
}
