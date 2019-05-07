/**
     * Battle Armor units can only get hit in undestroyed troopers.
     */
@Override
public HitData rollHitLocation(int table, int side, int aimedLocation, int aimingMode) {
    // If this squad was killed, target trooper 1 (just because).  
    if (isDoomed()) {
        return new HitData(1);
    }
    if ((aimedLocation != LOC_NONE) && (aimingMode != IAimingModes.AIM_MODE_NONE)) {
        int roll = Compute.d6(2);
        if ((5 < roll) && (roll < 9)) {
            return new HitData(aimedLocation, side == ToHitData.SIDE_REAR, true);
        }
    }
    // Pick a random number between 1 and 6.  
    int loc = Compute.d6();
    /*        if ((aimedLocation != LOC_NONE)
                && (aimingMode != IAimingModes.AIM_MODE_NONE)) {

            int roll = Compute.d6(2);

            if ((5 < roll) && (roll < 9)) {
                return new HitData(aimedLocation, side == ToHitData.SIDE_REAR,
                        true);
            }
        }*/
    // Pick a new random number if that trooper is dead or never existed.  
    // Remember that there's one more location than the number of troopers.  
    // In http://forums.classicbattletech.com/index.php/topic,43203.0.html,  
    // "previously destroyed includes the current phase" for rolling hits on  
    // a squad,  
    // modifying previous ruling in the AskThePM FAQ.  
    while ((loc >= locations()) || (IArmorState.ARMOR_NA == this.getInternal(loc)) || (IArmorState.ARMOR_DESTROYED == this.getInternal(loc)) || ((IArmorState.ARMOR_DOOMED == this.getInternal(loc)) && !isDoomed())) {
        loc = Compute.d6();
    }
    int critLocation = Compute.d6();
    //TacOps p. 108 Trooper takes a crit if a second roll is the same location as the first.  
    if (game.getOptions().booleanOption("tacops_ba_criticals") && (loc == critLocation)) {
        return new HitData(loc, false, HitData.EFFECT_CRITICAL);
    }
    // Hit that trooper.  
    return new HitData(loc);
}
