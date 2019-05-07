/**
     * Calculates the battle value of this tank
     */
@Override
public int calculateBattleValue(boolean ignoreC3, boolean ignorePilot) {
    double dbv = 0;
    // defensive battle value  
    double obv = 0;
    // offensive bv  
    int modularArmor = 0;
    for (Mounted mounted : getEquipment()) {
        if ((mounted.getType() instanceof MiscType) && mounted.getType().hasFlag(MiscType.F_MODULAR_ARMOR)) {
            modularArmor += mounted.getBaseDamageCapacity() - mounted.getDamageTaken();
        }
    }
    boolean blueShield = false;
    // a blueshield system means a +0.2 on the armor and internal modifier,  
    // like for mechs  
    if (hasWorkingMisc(MiscType.F_BLUE_SHIELD)) {
        blueShield = true;
    }
    // total armor points  
    dbv += (getTotalArmor() + modularArmor) * 2.5 * (blueShield ? 1.2 : 1);
    // total internal structure  
    dbv += getTotalInternal() * 1.5 * (blueShield ? 1.2 : 1);
    // add defensive equipment  
    double dEquipmentBV = 0;
    for (Mounted mounted : getEquipment()) {
        EquipmentType etype = mounted.getType();
        // don't count destroyed equipment  
        if (mounted.isDestroyed()) {
            continue;
        }
        if (((etype instanceof WeaponType) && (etype.hasFlag(WeaponType.F_AMS) || etype.hasFlag(WeaponType.F_B_POD))) || ((etype instanceof AmmoType) && (((AmmoType) etype).getAmmoType() == AmmoType.T_AMS))) {
            dEquipmentBV += etype.getBV(this);
        } else if ((etype instanceof MiscType) && (etype.hasFlag(MiscType.F_ECM) || etype.hasFlag(MiscType.F_AP_POD) || etype.hasFlag(MiscType.F_BAP))) {
            MiscType mtype = (MiscType) etype;
            double bv = mtype.getBV(this, mounted.getLocation());
            dEquipmentBV += bv;
        }
    }
    dbv += dEquipmentBV;
    double typeModifier;
    switch(getMovementMode()) {
        case TRACKED:
            typeModifier = 0.9;
            break;
        case WHEELED:
            typeModifier = 0.8;
            break;
        case HOVER:
        case VTOL:
        case WIGE:
            typeModifier = 0.7;
            break;
        case NAVAL:
            typeModifier = 0.6;
            break;
        default:
            typeModifier = 0.6;
    }
    if (hasWorkingMisc(MiscType.F_LIMITED_AMPHIBIOUS) || hasWorkingMisc(MiscType.F_DUNE_BUGGY) || hasWorkingMisc(MiscType.F_FLOTATION_HULL) || hasWorkingMisc(MiscType.F_VACUUM_PROTECTION) || hasWorkingMisc(MiscType.F_ENVIRONMENTAL_SEALING)) {
        typeModifier += .1;
    }
    if (hasWorkingMisc(MiscType.F_FULLY_AMPHIBIOUS)) {
        typeModifier += .2;
    }
    dbv *= typeModifier;
    // adjust for target movement modifier  
    double tmmRan = Compute.getTargetMovementModifier(getRunMP(false, true), this instanceof VTOL, this instanceof VTOL).getValue();
    // for the future, when we implement jumping tanks  
    int tmmJumped = Compute.getTargetMovementModifier(getJumpMP(), true, false).getValue();
    if (hasStealth()) {
        tmmRan += 2;
        tmmJumped += 2;
    }
    double tmmFactor = 1 + (Math.max(tmmRan, tmmJumped) / 10);
    dbv *= tmmFactor;
    double weaponBV = 0;
    // figure out base weapon bv  
    double weaponsBVFront = 0;
    double weaponsBVRear = 0;
    boolean hasTargComp = hasTargComp();
    // and add up BVs for ammo-using weapon types for excessive ammo rule  
    Map<String, Double> weaponsForExcessiveAmmo = new HashMap<String, Double>();
    for (Mounted mounted : getWeaponList()) {
        WeaponType wtype = (WeaponType) mounted.getType();
        double dBV = wtype.getBV(this);
        // don't count destroyed equipment  
        if (mounted.isDestroyed()) {
            continue;
        }
        // don't count AMS, it's defensive  
        if (wtype.hasFlag(WeaponType.F_AMS)) {
            continue;
        }
        if (wtype.hasFlag(WeaponType.F_B_POD)) {
            continue;
        }
        // artemis bumps up the value  
        if (mounted.getLinkedBy() != null) {
            Mounted mLinker = mounted.getLinkedBy();
            if ((mLinker.getType() instanceof MiscType) && mLinker.getType().hasFlag(MiscType.F_ARTEMIS)) {
                dBV *= 1.2;
            }
            if ((mLinker.getType() instanceof MiscType) && mLinker.getType().hasFlag(MiscType.F_ARTEMIS_V)) {
                dBV *= 1.3;
            }
        }
        if (mounted.getLinkedBy() != null) {
            Mounted mLinker = mounted.getLinkedBy();
            if ((mLinker.getType() instanceof MiscType) && mLinker.getType().hasFlag(MiscType.F_APOLLO)) {
                dBV *= 1.15;
            }
        }
        // and we'll add the tcomp here too  
        if (wtype.hasFlag(WeaponType.F_DIRECT_FIRE) && hasTargComp) {
            dBV *= 1.25;
        }
        if (mounted.getLocation() == LOC_REAR) {
            weaponsBVRear += dBV;
        } else if (mounted.getLocation() == LOC_FRONT) {
            weaponsBVFront += dBV;
        } else {
            weaponBV += dBV;
        }
        // add up BV of ammo-using weapons for each type of weapon,  
        // to compare with ammo BV later for excessive ammo BV rule  
        if (!((wtype.hasFlag(WeaponType.F_ENERGY) && !(wtype.getAmmoType() == AmmoType.T_PLASMA)) || wtype.hasFlag(WeaponType.F_ONESHOT) || wtype.hasFlag(WeaponType.F_INFANTRY) || (wtype.getAmmoType() == AmmoType.T_NA))) {
            String key = wtype.getAmmoType() + ":" + wtype.getRackSize();
            if (!weaponsForExcessiveAmmo.containsKey(key)) {
                weaponsForExcessiveAmmo.put(key, wtype.getBV(this));
            } else {
                weaponsForExcessiveAmmo.put(key, wtype.getBV(this) + weaponsForExcessiveAmmo.get(key));
            }
        }
    }
    if (weaponsBVFront > weaponsBVRear) {
        weaponBV += weaponsBVFront;
        weaponBV += (weaponsBVRear * 0.5);
    } else {
        weaponBV += weaponsBVRear;
        weaponBV += (weaponsBVFront * 0.5);
    }
    // add ammo bv  
    double ammoBV = 0;
    // extra BV for when we have semiguided LRMs and someone else has TAG on  
    // our team  
    double tagBV = 0;
    Map<String, Double> ammo = new HashMap<String, Double>();
    ArrayList<String> keys = new ArrayList<String>();
    for (Mounted mounted : getAmmo()) {
        AmmoType atype = (AmmoType) mounted.getType();
        // don't count depleted ammo  
        if (mounted.getShotsLeft() == 0) {
            continue;
        }
        // don't count AMS, it's defensive  
        if (atype.getAmmoType() == AmmoType.T_AMS) {
            continue;
        }
        // don't count oneshot ammo, it's considered part of the launcher.  
        if (mounted.getLocation() == Entity.LOC_NONE) {
            // assumption: ammo without a location is for a oneshot weapon  
            continue;
        }
        // semiguided or homing ammo might count double  
        if ((atype.getMunitionType() == AmmoType.M_SEMIGUIDED) || (atype.getMunitionType() == AmmoType.M_HOMING)) {
            Player tmpP = getOwner();
            // Okay, actually check for friendly TAG.  
            if (tmpP != null) {
                if (tmpP.hasTAG()) {
                    tagBV += atype.getBV(this);
                } else if ((tmpP.getTeam() != Player.TEAM_NONE) && (game != null)) {
                    for (Enumeration<Team> e = game.getTeams(); e.hasMoreElements(); ) {
                        Team m = e.nextElement();
                        if (m.getId() == tmpP.getTeam()) {
                            if (m.hasTAG(game)) {
                                tagBV += atype.getBV(this);
                            }
                            // A player can't be on two teams.  
                            // If we check his team and don't give the  
                            // penalty, that's it.  
                            break;
                        }
                    }
                }
            }
        }
        String key = atype.getAmmoType() + ":" + atype.getRackSize();
        if (!keys.contains(key)) {
            keys.add(key);
        }
        if (!ammo.containsKey(key)) {
            ammo.put(key, atype.getBV(this));
        } else {
            ammo.put(key, atype.getBV(this) + ammo.get(key));
        }
    }
    // excessive ammo rule:  
    // only count BV for ammo for a weapontype until the BV of all weapons  
    // of that  
    // type on the mech is reached  
    for (String key : keys) {
        // They dont exist in either hash then dont bother adding nulls.  
        if (!ammo.containsKey(key) || !weaponsForExcessiveAmmo.containsKey(key)) {
            continue;
        }
        if (ammo.get(key) > weaponsForExcessiveAmmo.get(key)) {
            ammoBV += weaponsForExcessiveAmmo.get(key);
        } else {
            ammoBV += ammo.get(key);
        }
    }
    weaponBV += ammoBV;
    // add offensive misc. equipment BV (everything except AMS, A-Pod, ECM -  
    // BMR p152)  
    double oEquipmentBV = 0;
    for (Mounted mounted : getMisc()) {
        MiscType mtype = (MiscType) mounted.getType();
        // don't count destroyed equipment  
        if (mounted.isDestroyed()) {
            continue;
        }
        if ((mtype.hasFlag(MiscType.F_ECM) && !mtype.hasFlag(MiscType.F_WATCHDOG)) || mtype.hasFlag(MiscType.F_AP_POD) || mtype.hasFlag(MiscType.F_BAP) || mtype.hasFlag(MiscType.F_TARGCOMP)) {
            // weapons  
            continue;
        }
        double bv = mtype.getBV(this, mounted.getLocation());
        // we need to special case watchdog, because it has both offensive  
        // and defensive BV  
        if (mtype.hasFlag(MiscType.F_WATCHDOG)) {
            bv = 7;
        }
        oEquipmentBV += bv;
    }
    weaponBV += oEquipmentBV;
    weaponBV += getWeight() / 2;
    // adjust further for speed factor  
    double speedFactor = Math.pow(1 + (((double) getRunMP(false, true) + (Math.round((double) getJumpMP(false) / 2)) - 5) / 10), 1.2);
    speedFactor = Math.round(speedFactor * 100) / 100.0;
    obv = weaponBV * speedFactor;
    // we get extra bv from some stuff  
    double xbv = 0.0;
    // extra BV for semi-guided lrm when TAG in our team  
    xbv += tagBV;
    // extra from c3 networks. a valid network requires at least 2 members  
    // some hackery and magic numbers here. could be better  
    // also, each 'has' loops through all equipment. inefficient to do it 3  
    // times  
    if (((hasC3MM() && (calculateFreeC3MNodes() < 2)) || (hasC3M() && (calculateFreeC3Nodes() < 3)) || (hasC3S() && (c3Master > NONE)) || (hasC3i() && (calculateFreeC3Nodes() < 5))) && !ignoreC3 && (game != null)) {
        int totalForceBV = 0;
        totalForceBV += this.calculateBattleValue(true, true);
        for (Entity e : game.getC3NetworkMembers(this)) {
            if (!equals(e) && onSameC3NetworkAs(e)) {
                totalForceBV += e.calculateBattleValue(true, true);
            }
        }
        xbv += totalForceBV *= 0.05;
    }
    int finalBV = (int) Math.round(dbv + obv + xbv);
    // and then factor in pilot  
    double pilotFactor = 1;
    if (!ignorePilot) {
        pilotFactor = crew.getBVSkillMultiplier();
    }
    int retVal = (int) Math.round((finalBV) * pilotFactor);
    // don't factor pilot in if we are just calculating BV for C3 extra BV  
    if (ignoreC3) {
        return finalBV;
    }
    return retVal;
}
