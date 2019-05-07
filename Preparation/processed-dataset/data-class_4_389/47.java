/**
     * Rolls up a hit location
     */
@Override
public HitData rollHitLocation(int table, int side, int aimedLocation, int aimingMode) {
    int nArmorLoc = LOC_FRONT;
    boolean bSide = false;
    boolean bRear = false;
    boolean ignoreTurret = m_bHasNoTurret || (table == ToHitData.HIT_UNDERWATER);
    int motiveMod = 0;
    if ((side == ToHitData.SIDE_FRONT) && isHullDown() && !ignoreTurret) {
        // on a hull down vee, all front hits go to turret if one exists.  
        nArmorLoc = LOC_TURRET;
    }
    if (side == ToHitData.SIDE_LEFT) {
        nArmorLoc = LOC_LEFT;
        bSide = true;
        motiveMod = 2;
    } else if (side == ToHitData.SIDE_RIGHT) {
        nArmorLoc = LOC_RIGHT;
        bSide = true;
        motiveMod = 2;
    } else if (side == ToHitData.SIDE_REAR) {
        nArmorLoc = LOC_REAR;
        motiveMod = 1;
        bRear = true;
    }
    if (game.getOptions().booleanOption("tacops_vehicle_effective")) {
        motiveMod = Math.max(0, motiveMod - 1);
    }
    HitData rv = new HitData(nArmorLoc);
    boolean bHitAimed = false;
    if ((aimedLocation != LOC_NONE) && (aimingMode != IAimingModes.AIM_MODE_NONE)) {
        int roll = Compute.d6(2);
        if ((5 < roll) && (roll < 9)) {
            rv = new HitData(aimedLocation, side == ToHitData.SIDE_REAR, true);
            bHitAimed = true;
        }
    }
    if (!bHitAimed) {
        switch(Compute.d6(2)) {
            case 2:
                rv.setEffect(HitData.EFFECT_CRITICAL);
                break;
            case 3:
                rv.setEffect(HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                rv.setMotiveMod(motiveMod);
                break;
            case 4:
                rv.setEffect(HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                rv.setMotiveMod(motiveMod);
                break;
            case 5:
                if (bSide) {
                    rv = new HitData(LOC_FRONT, false, HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                } else if (bRear) {
                    rv = new HitData(LOC_LEFT, false, HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                } else {
                    rv = new HitData(LOC_LEFT, false, HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                }
                rv.setMotiveMod(motiveMod);
                break;
            case 6:
            case 7:
                break;
            case 8:
                if (bSide && !game.getOptions().booleanOption("tacops_vehicle_effective")) {
                    rv.setEffect(HitData.EFFECT_CRITICAL);
                }
                break;
            case 9:
                if (game.getOptions().booleanOption("tacops_vehicle_effective")) {
                    if (bSide) {
                        rv = new HitData(LOC_REAR);
                    } else if (bRear) {
                        rv = new HitData(LOC_RIGHT);
                    } else {
                        rv = new HitData(LOC_LEFT);
                    }
                } else {
                    if (bSide) {
                        rv = new HitData(LOC_REAR, false, HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                    } else if (bRear) {
                        rv = new HitData(LOC_RIGHT, false, HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                    } else {
                        rv = new HitData(LOC_LEFT, false, HitData.EFFECT_VEHICLE_MOVE_DAMAGED);
                    }
                    rv.setMotiveMod(motiveMod);
                }
                break;
            case 10:
                if (!ignoreTurret) {
                    rv = new HitData(LOC_TURRET);
                }
                break;
            case 11:
                if (!ignoreTurret) {
                    rv = new HitData(LOC_TURRET);
                }
                break;
            case 12:
                if (ignoreTurret) {
                    rv.setEffect(HitData.EFFECT_CRITICAL);
                } else {
                    rv = new HitData(LOC_TURRET, false, HitData.EFFECT_CRITICAL);
                }
        }
    }
    if (table == ToHitData.HIT_SWARM) {
        rv.setEffect(rv.getEffect() | HitData.EFFECT_CRITICAL);
    }
    return rv;
}
