/**
     * used by the toHit of derived classes atype may be null if not using an
     * ammo based weapon
     */
public static ToHitData nightModifiers(IGame game, Targetable target, AmmoType atype, Entity attacker, boolean isWeapon) {
    ToHitData toHit = null;
    Entity te = null;
    if (target.getTargetType() == Targetable.TYPE_ENTITY) {
        te = (Entity) target;
    }
    toHit = new ToHitData();
    int lightCond = game.getPlanetaryConditions().getLight();
    if (lightCond == PlanetaryConditions.L_DAY) {
        //not nighttime so just return  
        return toHit;
    }
    // The base night penalty  
    int night_modifier = 0;
    night_modifier = game.getPlanetaryConditions().getLightHitPenalty(isWeapon);
    toHit.addModifier(night_modifier, game.getPlanetaryConditions().getLightCurrentName());
    boolean illuminated = false;
    if (te != null) {
        illuminated = te.isIlluminated();
        // hack for unresolved actions so client shows right BTH  
        if (!illuminated) {
            for (Enumeration<EntityAction> actions = game.getActions(); actions.hasMoreElements(); ) {
                EntityAction a = actions.nextElement();
                if (a instanceof SearchlightAttackAction) {
                    SearchlightAttackAction saa = (SearchlightAttackAction) a;
                    if (saa.willIlluminate(game, te)) {
                        illuminated = true;
                        break;
                    }
                }
            }
        }
    }
    // Searchlights reduce the penalty to zero (or 1 for pitch-black) (except for dusk/dawn)  
    int searchlightMod = Math.min(3, night_modifier);
    if ((te != null) && (lightCond > PlanetaryConditions.L_DUSK)) {
        if (te.isUsingSpotlight()) {
            toHit.addModifier(-searchlightMod, "target using searchlight");
            night_modifier = night_modifier - searchlightMod;
        } else if (illuminated) {
            toHit.addModifier(-searchlightMod, "target illuminated by searchlight");
            night_modifier = night_modifier - searchlightMod;
        }
    } else if (game.isPositionIlluminated(target.getPosition())) {
        toHit.addModifier(-night_modifier, "target illuminated by flare");
        night_modifier = 0;
    } else if (atype != null) {
        if (((atype.getAmmoType() == AmmoType.T_AC) || (atype.getAmmoType() == AmmoType.T_LAC)) && ((atype.getMunitionType() == AmmoType.M_INCENDIARY_AC) || (atype.getMunitionType() == AmmoType.M_TRACER))) {
            toHit.addModifier(-1, "incendiary/tracer ammo");
            night_modifier--;
        }
    }
    // Laser heatsinks  
    if ((night_modifier > 0) && (te != null) && (te instanceof Mech) && ((Mech) te).hasLaserHeatSinks()) {
        boolean lhsused = false;
        if (te.heat > 0) {
            toHit.addModifier(-night_modifier, "target overheated with laser heatsinks");
            night_modifier = 0;
        } else if ((te.heatBuildup > 0) || te.isStealthActive()) {
            lhsused = true;
        } else {
            // Unfortunately, we can't just check weapons fired by the  
            // target  
            // because isUsedThisRound() is not valid if the attacker  
            // declared first.  
            // therefore, enumerate WeaponAttackActions...  
            for (Enumeration<EntityAction> actions = game.getActions(); actions.hasMoreElements(); ) {
                EntityAction a = actions.nextElement();
                if (a instanceof WeaponAttackAction) {
                    WeaponAttackAction waa = (WeaponAttackAction) a;
                    if (waa.getEntityId() == te.getId()) {
                        Mounted weapon = te.getEquipment(waa.getWeaponId());
                        if ((weapon.getCurrentHeat() != 0) || weapon.isRapidfire()) {
                            // target fired a weapon that generates heat  
                            lhsused = true;
                            break;
                        }
                    }
                }
            }
        }
        if (lhsused) {
            toHit.addModifier(-1, "target uses laser heatsinks");
        }
    }
    //now check for general hit bonuses for heat  
    if ((te != null) && !((attacker instanceof Infantry) && !(attacker instanceof BattleArmor))) {
        int heatBonus = game.getPlanetaryConditions().getLightHeatBonus(te.heat);
        if (heatBonus < 0) {
            toHit.addModifier(heatBonus, "target excess heat at night");
        }
    }
    return toHit;
}
