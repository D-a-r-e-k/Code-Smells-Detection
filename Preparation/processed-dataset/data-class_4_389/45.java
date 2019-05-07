/**
     * Returns the Compute.ARC that the weapon fires into.
     */
@Override
public int getWeaponArc(int wn) {
    final Mounted mounted = getEquipment(wn);
    // B-Pods need to be special-cased, the have 360 firing arc  
    if ((mounted.getType() instanceof WeaponType) && mounted.getType().hasFlag(WeaponType.F_B_POD)) {
        return Compute.ARC_360;
    }
    switch(mounted.getLocation()) {
        case LOC_BODY:
        // Body mounted C3Ms fire into the front arc,  
        // per  
        // http://forums.classicbattletech.com/index.php/topic,9400.0.html  
        case LOC_FRONT:
            if (game.getOptions().booleanOption("tacops_vehicle_arcs")) {
                return Compute.ARC_NOSE;
            }
        case LOC_TURRET:
            if (game.getOptions().booleanOption("tacops_vehicle_arcs")) {
                return Compute.ARC_TURRET;
            }
            return Compute.ARC_FORWARD;
        case LOC_RIGHT:
            if (game.getOptions().booleanOption("tacops_vehicle_arcs")) {
                return Compute.ARC_RIGHT_BROADSIDE;
            }
            return Compute.ARC_RIGHTSIDE;
        case LOC_LEFT:
            if (game.getOptions().booleanOption("tacops_vehicle_arcs")) {
                return Compute.ARC_LEFT_BROADSIDE;
            }
            return Compute.ARC_LEFTSIDE;
        case LOC_REAR:
            if (game.getOptions().booleanOption("tacops_vehicle_arcs")) {
                return Compute.ARC_AFT;
            }
            return Compute.ARC_REAR;
        default:
            return Compute.ARC_360;
    }
}
