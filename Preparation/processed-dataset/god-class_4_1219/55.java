@Override
public boolean loadWeaponWithSameAmmo(Mounted mounted, Mounted mountedAmmo) {
    // BA must carry the ammo in same location as the weapon.  
    // except for mine launcher mines  
    // This allows for squad weapons and individual trooper weapons  
    // such as NARC and the support weapons in TW/TO  
    AmmoType at = (AmmoType) mountedAmmo.getType();
    if (!(at.getAmmoType() == AmmoType.T_MINE) && (mounted.getLocation() != mountedAmmo.getLocation())) {
        return false;
    }
    return super.loadWeaponWithSameAmmo(mounted, mountedAmmo);
}
