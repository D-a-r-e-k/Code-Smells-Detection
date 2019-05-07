/**
     * Calculates the battle value of this platoon.
     * @param ignoreC3 ignore C3 linkage
     * @param ignorePilot ignore the skill of the pilot
     * @param singleTrooper calculate just the BV of a single trooper
     * @return the battlevalue
     */
public int calculateBattleValue(boolean ignoreC3, boolean ignorePilot, boolean singleTrooper) {
    // we do this per trooper, then add up  
    double squadBV = 0;
    for (int i = 1; i < locations(); i++) {
        if (this.getInternal(i) <= 0) {
            continue;
        }
        double dBV = 0;
        double armorBV = 2.5;
        if (isFireResistant()) {
            armorBV = 3.5;
        }
        dBV += getArmor(i) * armorBV + 1;
        // improved sensors add 1  
        if (hasImprovedSensors()) {
            dBV += 1;
        }
        // active probes add 1  
        if (hasActiveProbe()) {
            dBV += 1;
        }
        // ECM adds 1  
        for (Mounted mounted : getMisc()) {
            if (mounted.getType().hasFlag(MiscType.F_ECM)) {
                dBV += 1;
                break;
            }
        }
        int runMP = getRunMP(false, false);
        int tmmRan = Compute.getTargetMovementModifier(runMP, false, false).getValue();
        // get jump MP, ignoring burden  
        int rawJump = getJumpMP(false, true);
        int tmmJumped = Compute.getTargetMovementModifier(rawJump, true, false).getValue();
        double targetMovementModifier = Math.max(tmmRan, tmmJumped);
        double tmmFactor = 1 + (targetMovementModifier / 10) + 0.1;
        if (isSimpleCamo) {
            tmmFactor += 0.2;
        }
        if (isStealthy) {
            tmmFactor += 0.2;
        }
        // improved stealth get's an extra 0.1, for 0.3 total  
        if ((stealthName != null) && stealthName.equals(BattleArmor.EXPERT_STEALTH)) {
            tmmFactor += 0.1;
        }
        if (isMimetic) {
            tmmFactor += 0.3;
        }
        dBV *= tmmFactor;
        double oBV = 0;
        for (Mounted weapon : getWeaponList()) {
            // infantry weapons don't count at all  
            if (weapon.getType().hasFlag(WeaponType.F_INFANTRY)) {
                continue;
            }
            if (weapon.getLocation() == LOC_SQUAD) {
                oBV += weapon.getType().getBV(this);
            } else {
                // squad support, count at 1/troopercount  
                oBV += weapon.getType().getBV(this) / getTotalOInternal();
            }
        }
        for (Mounted misc : getMisc()) {
            if (misc.getType().hasFlag(MiscType.F_MINE)) {
                if (misc.getLocation() == LOC_SQUAD) {
                    oBV += misc.getType().getBV(this);
                } else {
                    oBV += misc.getType().getBV(this) / getTotalOInternal();
                }
            }
        }
        for (Mounted ammo : getAmmo()) {
            int loc = ammo.getLocation();
            // don't count oneshot ammo  
            if (loc == LOC_NONE) {
                continue;
            }
            if ((loc == LOC_SQUAD) || (loc == i)) {
                double ammoBV = ((AmmoType) ammo.getType()).getBABV();
                oBV += ammoBV;
            }
        }
        if (isAntiMek()) {
            // all non-missile and non-body mounted direct fire weapons  
            // counted again  
            for (Mounted weapon : getWeaponList()) {
                // infantry weapons don't count at all  
                if (weapon.getType().hasFlag(WeaponType.F_INFANTRY)) {
                    continue;
                }
                if (weapon.getLocation() == LOC_SQUAD) {
                    if (!weapon.getType().hasFlag(WeaponType.F_MISSILE) && !weapon.isBodyMounted()) {
                        oBV += weapon.getType().getBV(this);
                    }
                } else {
                    // squad support, count at 1/troopercount  
                    oBV += weapon.getType().getBV(this) / getTotalOInternal();
                }
            }
            // magnetic claws and vibro claws counted again  
            for (Mounted misc : getMisc()) {
                if ((misc.getLocation() == LOC_SQUAD) || (misc.getLocation() == i)) {
                    if (misc.getType().hasFlag(MiscType.F_MAGNET_CLAW) || misc.getType().hasFlag(MiscType.F_VIBROCLAW)) {
                        oBV += misc.getType().getBV(this);
                    }
                }
            }
        }
        int movement = Math.max(getWalkMP(false, false), getJumpMP(false, true));
        double speedFactor = Math.pow(1 + ((double) (movement - 5) / 10), 1.2);
        speedFactor = Math.round(speedFactor * 100) / 100.0;
        oBV *= speedFactor;
        squadBV += dBV;
        /*
            if (i == 1) {
                System.out.println(this.getChassis()+this.getModel());
                System.out.println(dBV);
                System.out.println(oBV);
                System.out.println((oBV+dBV));
            }*/
        squadBV += oBV;
    }
    // we have now added all troopers, divide by current strength to then  
    // multiply by the unit size mod  
    squadBV /= getShootingStrength();
    // we might want to get just the BV of a single trooper  
    if (singleTrooper) {
        return (int) Math.round(squadBV);
    }
    switch(getShootingStrength()) {
        case 1:
            break;
        case 2:
            squadBV *= 2.2;
            break;
        case 3:
            squadBV *= 3.6;
            break;
        case 4:
            squadBV *= 5.2;
            break;
        case 5:
            squadBV *= 7;
            break;
        case 6:
            squadBV *= 9;
            break;
    }
    // Adjust BV for crew skills.  
    double pilotFactor = 1;
    if (!ignorePilot) {
        pilotFactor = crew.getBVSkillMultiplier(isAntiMek());
    }
    int retVal = (int) Math.round(squadBV * pilotFactor);
    return retVal;
}
