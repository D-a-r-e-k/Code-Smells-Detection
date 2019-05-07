@Override
public int getBattleForceStandardWeaponsDamage(int range, int ammoType, boolean ignoreHeat, boolean ignoreSpecialAbility) {
    double totalDamage = 0;
    double frontArcWeaponsTotalDamage = 0;
    double rearArcWeaponsTotalDamage = 0;
    double totalHeat = 0;
    boolean hasArtemis = false;
    boolean hasTC = hasTargComp();
    double baseDamage = 0;
    TreeSet<String> weaponsUsed = new TreeSet<String>();
    ArrayList<Mounted> weaponsList = getWeaponList();
    if (DEBUGBATTLEFORCE) {
        battleForceDebugString.append('\n');
        battleForceDebugString.append("Weapons Range: ");
        battleForceDebugString.append(range);
        battleForceDebugString.append('\n');
    }
    for (int pos = 0; pos < weaponList.size(); pos++) {
        double damageModifier = 1;
        double weaponCount = 1;
        double minRangeDamageModifier = 1;
        hasArtemis = false;
        Mounted mount = weaponsList.get(pos);
        if ((mount == null) || mount.isRearMounted() || weaponsUsed.contains(mount.getName())) {
            continue;
        }
        WeaponType weapon = (WeaponType) mount.getType();
        if ((weapon.getLongRange() < range) && !(weapon instanceof ISLAC5) && !(weapon instanceof ATMWeapon) && !(weapon instanceof MMLWeapon)) {
            continue;
        }
        if ((ammoType != AmmoType.T_NA) && (weapon.getAmmoType() != ammoType)) {
            continue;
        }
        if ((weapon.getAmmoType() == AmmoType.T_INARC) || (weapon.getAmmoType() == AmmoType.T_NARC)) {
            continue;
        }
        // Check ammo weapons first since they had a hidden modifier  
        if ((weapon.getAmmoType() != AmmoType.T_NA) && !weapon.hasFlag(WeaponType.F_ONESHOT)) {
            weaponsUsed.add(weapon.getName());
            for (int nextPos = pos + 1; nextPos < weaponList.size(); nextPos++) {
                Mounted nextWeapon = weaponList.get(nextPos);
                if ((nextWeapon == null) || nextWeapon.isRearMounted()) {
                    continue;
                }
                if (nextWeapon.getType().equals(weapon)) {
                    weaponCount++;
                }
            }
            int ammoCount = 0;
            // Check if they have enough ammo for all the guns to last at  
            // least 10 rounds  
            for (Mounted ammo : getAmmo()) {
                AmmoType at = (AmmoType) ammo.getType();
                if ((at.getAmmoType() == weapon.getAmmoType()) && (at.getRackSize() == weapon.getRackSize())) {
                    // RACs are always fired on 6 shot so that means you  
                    // need 6 times the ammo to avoid the ammo damage  
                    // modifier  
                    if (at.getAmmoType() == AmmoType.T_AC_ROTARY) {
                        ammoCount += at.getShots() / 6;
                    } else {
                        ammoCount += at.getShots();
                    }
                }
            }
            if (DEBUGBATTLEFORCE) {
                battleForceDebugString.append("Ammo Weapon ");
                battleForceDebugString.append(weapon.getName());
                battleForceDebugString.append(" ammo count: ");
                battleForceDebugString.append(ammoCount);
                battleForceDebugString.append('\n');
                battleForceDebugString.append("Weapon Count: ");
                battleForceDebugString.append(weaponCount);
                battleForceDebugString.append('\n');
            }
            if (ammoCount / weaponCount < 10) {
                damageModifier *= .75;
                if (DEBUGBATTLEFORCE) {
                    battleForceDebugString.append("Damage Modifier *.75: ");
                    battleForceDebugString.append(damageModifier);
                    battleForceDebugString.append('\n');
                }
            }
        }
        if (weapon.hasFlag(WeaponType.F_MISSILE)) {
            baseDamage = Compute.calculateClusterHitTableAmount(7, weapon.getRackSize());
            baseDamage *= weaponCount;
        } else {
            baseDamage = weapon.getDamage() * weaponCount;
        }
        if (DEBUGBATTLEFORCE) {
            battleForceDebugString.append("Base Damage: ");
            battleForceDebugString.append(baseDamage);
            battleForceDebugString.append('\n');
        }
        if (range == Entity.BATTLEFORCESHORTRANGE) {
            int minRange = Math.min(6, Math.max(0, weapon.getMinimumRange()));
            minRangeDamageModifier *= battleForceMinRangeModifier[minRange];
            if (DEBUGBATTLEFORCE && (minRange > 0)) {
                battleForceDebugString.append("Min range damage modifier: ");
                battleForceDebugString.append(minRangeDamageModifier);
                battleForceDebugString.append('\n');
            }
        }
        int toHitMod = weapon.getToHitModifier() + 4;
        switch(weapon.getAmmoType()) {
            case AmmoType.T_AC_LBX:
            case AmmoType.T_AC_LBX_THB:
                baseDamage = Compute.calculateClusterHitTableAmount(7, weapon.getRackSize()) * weaponCount;
                toHitMod--;
                break;
            case AmmoType.T_MRM:
                Mounted mLinker = mount.getLinkedBy();
                if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_APOLLO))) {
                    toHitMod--;
                    baseDamage = Compute.calculateClusterHitTableAmount(6, weapon.getRackSize()) * weaponCount;
                }
                break;
            case AmmoType.T_LRM:
                mLinker = mount.getLinkedBy();
                if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_ARTEMIS))) {
                    baseDamage = Compute.calculateClusterHitTableAmount(9, weapon.getRackSize()) * weaponCount;
                    hasArtemis = true;
                } else if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_ARTEMIS_V))) {
                    baseDamage = Compute.calculateClusterHitTableAmount(10, weapon.getRackSize()) * weaponCount;
                    hasArtemis = true;
                }
                break;
            case AmmoType.T_SRM:
                mLinker = mount.getLinkedBy();
                if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_ARTEMIS))) {
                    baseDamage = Compute.calculateClusterHitTableAmount(9, weapon.getRackSize()) * 2 * weaponCount;
                    hasArtemis = true;
                } else if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_ARTEMIS_V))) {
                    baseDamage = Compute.calculateClusterHitTableAmount(10, weapon.getRackSize()) * 2 * weaponCount;
                    hasArtemis = true;
                } else {
                    baseDamage = Compute.calculateClusterHitTableAmount(7, weapon.getRackSize()) * 2 * weaponCount;
                }
                break;
            case AmmoType.T_ATM:
                minRangeDamageModifier = 1;
                switch(range) {
                    case Entity.BATTLEFORCESHORTRANGE:
                        baseDamage = Compute.calculateClusterHitTableAmount(9, weapon.getRackSize()) * weaponCount * 3;
                        break;
                    case Entity.BATTLEFORCEMEDIUMRANGE:
                        baseDamage = Compute.calculateClusterHitTableAmount(9, weapon.getRackSize()) * weaponCount * 2;
                        break;
                    case Entity.BATTLEFORCELONGRANGE:
                        baseDamage = Compute.calculateClusterHitTableAmount(9, weapon.getRackSize()) * weaponCount;
                        break;
                }
                break;
            case AmmoType.T_AC_ULTRA:
            case AmmoType.T_AC_ULTRA_THB:
                damageModifier *= 1.5;
                break;
            case AmmoType.T_HAG:
                switch(range) {
                    case Entity.BATTLEFORCESHORTRANGE:
                        baseDamage = Compute.calculateClusterHitTableAmount(9, weapon.getRackSize()) * weaponCount;
                        break;
                    case Entity.BATTLEFORCELONGRANGE:
                        baseDamage = Compute.calculateClusterHitTableAmount(5, weapon.getRackSize()) * weaponCount;
                        break;
                    case Entity.BATTLEFORCEMEDIUMRANGE:
                        baseDamage = Compute.calculateClusterHitTableAmount(7, weapon.getRackSize()) * weaponCount;
                        break;
                }
                break;
            case AmmoType.T_SRM_STREAK:
                baseDamage = weapon.getRackSize() * 2 * weaponCount;
                break;
            case AmmoType.T_AC_ROTARY:
                baseDamage = Compute.calculateClusterHitTableAmount(7, weapon.getRackSize()) * weaponCount * 5;
                break;
        }
        if (weapon instanceof ISSnubNosePPC) {
            switch(range) {
                case Entity.BATTLEFORCESHORTRANGE:
                    baseDamage = 10;
                    break;
                case Entity.BATTLEFORCELONGRANGE:
                    baseDamage = 0;
                    break;
                case Entity.BATTLEFORCEMEDIUMRANGE:
                    baseDamage = 5;
                    break;
            }
        }
        if (weapon instanceof VariableSpeedPulseLaserWeapon) {
            switch(range) {
                case Entity.BATTLEFORCESHORTRANGE:
                    toHitMod = 1;
                    break;
                case Entity.BATTLEFORCEMEDIUMRANGE:
                    toHitMod = 2;
                    break;
                case Entity.BATTLEFORCELONGRANGE:
                    toHitMod = 3;
                    break;
            }
        }
        damageModifier *= battleForceToHitModifier[toHitMod];
        if (DEBUGBATTLEFORCE) {
            battleForceDebugString.append("Base Damage: ");
            battleForceDebugString.append(baseDamage);
            battleForceDebugString.append('\n');
            battleForceDebugString.append("To Hit Modifier Damage Modifier: ");
            battleForceDebugString.append(damageModifier);
            battleForceDebugString.append('\n');
        }
        if (weapon.hasFlag(WeaponType.F_ONESHOT)) {
            damageModifier *= .1;
            if (DEBUGBATTLEFORCE) {
                battleForceDebugString.append("One Shot Modifier Damage Modifier: ");
                battleForceDebugString.append(damageModifier);
                battleForceDebugString.append('\n');
            }
        }
        if (hasTC && weapon.hasFlag(WeaponType.F_DIRECT_FIRE) && (weapon.getAmmoType() != AmmoType.T_AC_LBX) && (weapon.getAmmoType() != AmmoType.T_AC_LBX_THB)) {
            damageModifier *= 1.10;
            if (DEBUGBATTLEFORCE) {
                battleForceDebugString.append("TC Modifier Damage Modifier: ");
                battleForceDebugString.append(damageModifier);
                battleForceDebugString.append('\n');
            }
        }
        if ((weapon.getAmmoType() == AmmoType.T_LRM) || (weapon.getAmmoType() == AmmoType.T_AC) || (weapon.getAmmoType() == AmmoType.T_LAC) || (weapon.getAmmoType() == AmmoType.T_SRM)) {
            double damage = baseDamage * damageModifier;
            // if damage is greater then 10 then we do not add it to the  
            // standard damage it will be used in special weapons  
            if (((damage < 10) && !ignoreSpecialAbility) || (ignoreSpecialAbility && !hasArtemis) || (!ignoreSpecialAbility && hasArtemis)) {
                if (range == Entity.BATTLEFORCESHORTRANGE) {
                    damage *= minRangeDamageModifier;
                }
                frontArcWeaponsTotalDamage += damage;
                if (DEBUGBATTLEFORCE) {
                    battleForceDebugString.append("LRM/SRM/AC/MML Damage No Special Ability: ");
                    battleForceDebugString.append(damage);
                    battleForceDebugString.append('\n');
                }
            }
        } else if (weapon.hasFlag(WeaponType.F_PPC)) {
            Mounted mLinker = mount.getLinkedBy();
            if (range == Entity.BATTLEFORCESHORTRANGE) {
                baseDamage *= minRangeDamageModifier;
            }
            if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_PPC_CAPACITOR))) {
                frontArcWeaponsTotalDamage += ((baseDamage + 5) * .5) * damageModifier;
                if (DEBUGBATTLEFORCE) {
                    battleForceDebugString.append("PPC with Cap Damage: ");
                    battleForceDebugString.append(((baseDamage + 5) * .5) * damageModifier);
                    battleForceDebugString.append('\n');
                }
            } else {
                frontArcWeaponsTotalDamage += baseDamage * damageModifier;
                if (DEBUGBATTLEFORCE) {
                    battleForceDebugString.append("PPC Damage: ");
                    battleForceDebugString.append(baseDamage * damageModifier);
                    battleForceDebugString.append('\n');
                }
            }
        } else if (weapon.getAmmoType() == AmmoType.T_MML) {
            double ammoDamage = 1;
            Mounted mLinker = mount.getLinkedBy();
            if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_ARTEMIS))) {
                baseDamage = Compute.calculateClusterHitTableAmount(9, weapon.getRackSize()) * weaponCount;
                hasArtemis = true;
            } else if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_ARTEMIS_V))) {
                baseDamage = Compute.calculateClusterHitTableAmount(10, weapon.getRackSize()) * weaponCount;
                hasArtemis = true;
            }
            switch(range) {
                case Entity.BATTLEFORCESHORTRANGE:
                    ammoDamage = 2;
                    break;
                case Entity.BATTLEFORCELONGRANGE:
                    ammoDamage = 1;
                    break;
                case Entity.BATTLEFORCEMEDIUMRANGE:
                    ammoDamage = 1;
                    baseDamage = Math.round((baseDamage * 3) / 2);
                    break;
            }
            double damage = baseDamage * damageModifier;
            // if damage is greater then 10 then we do not add it to the  
            // standard damage it will be used in special weapons  
            if (((damage < 10) && !ignoreSpecialAbility) || (ignoreSpecialAbility && !hasArtemis && (damage >= 10)) || (!ignoreSpecialAbility && hasArtemis)) {
                frontArcWeaponsTotalDamage += damage * ammoDamage;
                if (DEBUGBATTLEFORCE) {
                    battleForceDebugString.append("MML Damage: ");
                    battleForceDebugString.append(baseDamage * ammoDamage * damageModifier);
                    battleForceDebugString.append('\n');
                }
            }
        } else {
            if (range == Entity.BATTLEFORCESHORTRANGE) {
                baseDamage *= minRangeDamageModifier;
            }
            frontArcWeaponsTotalDamage += baseDamage * damageModifier;
            if (DEBUGBATTLEFORCE) {
                battleForceDebugString.append(weapon.getName());
                battleForceDebugString.append(" Damage: ");
                battleForceDebugString.append(baseDamage * damageModifier);
                battleForceDebugString.append('\n');
            }
        }
    }
    totalDamage = Math.max(frontArcWeaponsTotalDamage, rearArcWeaponsTotalDamage);
    if (DEBUGBATTLEFORCE) {
        battleForceDebugString.append("Total Damage: ");
        battleForceDebugString.append(totalDamage);
        battleForceDebugString.append('\n');
    }
    totalHeat = getBattleForceTotalHeatGeneration(false) - 4;
    if ((totalHeat > getHeatCapacity()) && !ignoreHeat) {
        if (DEBUGBATTLEFORCE) {
            battleForceDebugString.append("Total Heat -4: ");
            battleForceDebugString.append(totalHeat);
            battleForceDebugString.append('\n');
            battleForceDebugString.append("Total Damage: ");
            battleForceDebugString.append(totalDamage);
            battleForceDebugString.append('\n');
            battleForceDebugString.append(totalDamage);
            battleForceDebugString.append(" * ");
            battleForceDebugString.append(getHeatCapacity());
            battleForceDebugString.append(" / ");
            battleForceDebugString.append(totalHeat);
            battleForceDebugString.append(" = ");
            battleForceDebugString.append((totalDamage * getHeatCapacity()) / totalHeat);
            battleForceDebugString.append('\n');
        }
        totalDamage = Math.ceil((totalDamage * getHeatCapacity()) / totalHeat);
    }
    if (ignoreSpecialAbility && (totalDamage < 10)) {
        totalDamage = 0;
    } else if ((ammoType != AmmoType.T_NA)) {
        totalDamage = Math.round(totalDamage / 10);
    } else {
        totalDamage = Math.ceil(totalDamage / 10);
    }
    return (int) totalDamage;
}
