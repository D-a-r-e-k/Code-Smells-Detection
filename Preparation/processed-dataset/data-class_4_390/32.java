/**
     * Determines if the battle armor unit is burdened with un-jettisoned
     * equipment. This can prevent the unit from jumping or using their special
     * Anti-Mek attacks.
     *
     * @return <code>true</code> if the unit hasn't jettisoned its equipment
     *         yet, <code>false</code> if it has.
     */
public boolean isBurdened() {
    // Clan Elemental points are never burdened by equipment.  
    if (!isClan()) {
        // if we have ammo left for a body mounted missile launcher,  
        // we are burdened  
        for (Mounted mounted : getAmmo()) {
            if (mounted.getShotsLeft() == 0) {
                // no shots left, we don't count  
                continue;
            }
            // first get the weapon we are linked by  
            // (so we basically only check the currently loaded  
            // ammo, but if the weapon has no currently loaded ammo, we're  
            // fine  
            Mounted weapon = mounted.getLinkedBy();
            if ((weapon != null) && weapon.isBodyMounted() && weapon.getType().hasFlag(WeaponType.F_MISSILE)) {
                return true;
            }
        }
    }
    // End is-inner-sphere-squad  
    // Unit isn't burdened.  
    return false;
}
