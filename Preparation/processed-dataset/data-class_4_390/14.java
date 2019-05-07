/**
     * For level 3 rules, each trooper occupies a specific location
     * precondition: hit is a location covered by BA
     */
@Override
public HitData getTrooperAtLocation(HitData hit, Entity transport) {
    if (transport instanceof Mech) {
        int loc = 99;
        switch(hit.getLocation()) {
            case Mech.LOC_RT:
                if (hit.isRear()) {
                    loc = 3;
                } else {
                    loc = 1;
                }
                break;
            case Mech.LOC_LT:
                if (hit.isRear()) {
                    loc = 4;
                } else {
                    loc = 2;
                }
                break;
            case Mech.LOC_CT:
                if (hit.isRear()) {
                    loc = 5;
                } else {
                    loc = 6;
                }
                break;
        }
        if (loc < locations()) {
            return new HitData(loc);
        }
    } else if (transport instanceof Tank) {
        int loc = 99;
        switch(hit.getLocation()) {
            case Tank.LOC_RIGHT:
                // There are 2 troopers on each location, so pick  
                // one randomly if both are alive.  
                if ((getInternal(1) > 0) && (getInternal(2) > 0)) {
                    loc = Compute.randomInt(2) + 1;
                } else if (getInternal(1) > 0) {
                    loc = 1;
                } else {
                    loc = 2;
                }
                break;
            case Tank.LOC_LEFT:
                if ((getInternal(3) > 0) && (getInternal(4) > 0)) {
                    loc = Compute.randomInt(2) + 3;
                } else if (getInternal(3) > 0) {
                    loc = 3;
                } else {
                    loc = 4;
                }
                break;
            case Tank.LOC_REAR:
                if ((getInternal(5) > 0) && (getInternal(6) > 0)) {
                    loc = Compute.randomInt(2) + 5;
                } else if (getInternal(5) > 0) {
                    loc = 5;
                } else {
                    loc = 6;
                }
                break;
        }
        if (loc < locations()) {
            return new HitData(loc);
        }
    }
    // otherwise roll a random location  
    return rollHitLocation(ToHitData.HIT_NORMAL, ToHitData.SIDE_FRONT);
}
