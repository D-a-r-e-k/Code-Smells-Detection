/**
     * get the type of critical caused by a critical roll, taking account of
     * existing damage
     * 
     * @param roll
     *            the final dice roll
     * @param loc
     *            the hit location
     * @return a critical type
     */
public int getCriticalEffect(int roll, int loc) {
    if (roll > 12) {
        roll = 12;
    }
    if (roll < 6) {
        return CRIT_NONE;
    }
    for (int i = 0; i < 2; i++) {
        if (i > 0) {
            roll = 6;
        }
        if (loc == LOC_FRONT) {
            switch(roll) {
                case 6:
                    if (!crew.isDead() && !crew.isDoomed()) {
                        if (!isDriverHit()) {
                            return CRIT_DRIVER;
                        } else if (!isCommanderHit()) {
                            return CRIT_CREW_STUNNED;
                        } else {
                            return CRIT_CREW_KILLED;
                        }
                    }
                case 7:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isJammed() && !m.isHit()) {
                            return CRIT_WEAPON_JAM;
                        }
                    }
                case 8:
                    if (!isStabiliserHit(loc)) {
                        for (Mounted m : getWeaponList()) {
                            if (m.getLocation() == loc) {
                                return CRIT_STABILIZER;
                            }
                        }
                    }
                case 9:
                    if (getSensorHits() < 4) {
                        return CRIT_SENSOR;
                    }
                case 10:
                    if (!crew.isDead() && !crew.isDoomed()) {
                        if (!isCommanderHit()) {
                            return CRIT_COMMANDER;
                        } else if (!isDriverHit()) {
                            return CRIT_CREW_STUNNED;
                        } else {
                            return CRIT_CREW_KILLED;
                        }
                    }
                case 11:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isHit()) {
                            return CRIT_WEAPON_DESTROYED;
                        }
                    }
                case 12:
                    if (!crew.isDead() && !crew.isDoomed()) {
                        return CRIT_CREW_KILLED;
                    }
            }
        } else if (loc == LOC_REAR) {
            switch(roll) {
                case 6:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isJammed() && !m.isHit()) {
                            return CRIT_WEAPON_JAM;
                        }
                    }
                case 7:
                    if (getLoadedUnits().size() > 0) {
                        return CRIT_CARGO;
                    }
                case 8:
                    if (!isStabiliserHit(loc)) {
                        for (Mounted m : getWeaponList()) {
                            if (m.getLocation() == loc) {
                                return CRIT_STABILIZER;
                            }
                        }
                    }
                case 9:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isHit()) {
                            return CRIT_WEAPON_DESTROYED;
                        }
                    }
                case 10:
                    if (!engineHit) {
                        return CRIT_ENGINE;
                    }
                case 11:
                    for (Mounted m : getAmmo()) {
                        if (!m.isDestroyed() && !m.isHit()) {
                            return CRIT_AMMO;
                        }
                    }
                case 12:
                    if (getEngine().isFusion() && !engineHit) {
                        return CRIT_ENGINE;
                    } else if (!getEngine().isFusion()) {
                        return CRIT_FUEL_TANK;
                    }
            }
        } else if (loc == LOC_TURRET) {
            switch(roll) {
                case 6:
                    if (!isStabiliserHit(loc)) {
                        for (Mounted m : getWeaponList()) {
                            if (m.getLocation() == loc) {
                                return CRIT_STABILIZER;
                            }
                        }
                    }
                case 7:
                    if (!isTurretLocked()) {
                        return CRIT_TURRET_JAM;
                    }
                case 8:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isJammed()) {
                            return CRIT_WEAPON_JAM;
                        }
                    }
                case 9:
                    if (!isTurretLocked()) {
                        return CRIT_TURRET_LOCK;
                    }
                case 10:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isHit()) {
                            return CRIT_WEAPON_DESTROYED;
                        }
                    }
                case 11:
                    for (Mounted m : getAmmo()) {
                        if (!m.isDestroyed() && !m.isHit()) {
                            return CRIT_AMMO;
                        }
                    }
                case 12:
                    return CRIT_TURRET_DESTROYED;
            }
        } else {
            switch(roll) {
                case 6:
                    if (getLoadedUnits().size() > 0) {
                        return CRIT_CARGO;
                    }
                case 7:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isJammed() && !m.isHit()) {
                            return CRIT_WEAPON_JAM;
                        }
                    }
                case 8:
                    if (!crew.isDead() && !crew.isDoomed()) {
                        if (isCommanderHit() && isDriverHit()) {
                            return CRIT_CREW_KILLED;
                        }
                        return CRIT_CREW_STUNNED;
                    }
                case 9:
                    if (!isStabiliserHit(loc)) {
                        for (Mounted m : getWeaponList()) {
                            if (m.getLocation() == loc) {
                                return CRIT_STABILIZER;
                            }
                        }
                    }
                case 10:
                    for (Mounted m : getWeaponList()) {
                        if ((m.getLocation() == loc) && !m.isDestroyed() && !m.isHit()) {
                            return CRIT_WEAPON_DESTROYED;
                        }
                    }
                case 11:
                    if (!engineHit) {
                        return CRIT_ENGINE;
                    }
                case 12:
                    if (getEngine().isFusion() && !engineHit) {
                        return CRIT_ENGINE;
                    } else if (!getEngine().isFusion()) {
                        return CRIT_FUEL_TANK;
                    }
            }
        }
    }
    return CRIT_NONE;
}
