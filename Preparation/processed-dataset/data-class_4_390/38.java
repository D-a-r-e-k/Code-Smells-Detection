/**
     * Determine the stealth modifier for firing at this unit from the given
     * range. If the value supplied for <code>range</code> is not one of the
     * <code>Entity</code> class range constants, an
     * <code>IllegalArgumentException</code> will be thrown. <p/> Sub-classes
     * are encouraged to override this method.
     *
     * @param range - an <code>int</code> value that must match one of the
     *            <code>Compute</code> class range constants.
     * @param ae - the entity making the attack.
     * @return a <code>TargetRoll</code> value that contains the stealth
     *         modifier for the given range.
     */
@Override
public TargetRoll getStealthModifier(int range, Entity ae) {
    TargetRoll result = null;
    // Note: infantry are immune to stealth, but not camoflage  
    // or mimetic armor  
    // TODO: eliminate duplicate code  
    if (armorType != -1) {
        /*
             * Here, in order, are the armor types used by custom Battle Armor
             * at this point: 0: "Standard", 1: "Advanced", 2: "Prototype", 3:
             * "Basic Stealth", 4: "Prototype Stealth", 5: "Standard Stealth",
             * 6: "Improved Stealth", 7: "Fire Resistant", 8: "Mimetic"
             */
        if ((armorType == 3) && !((ae instanceof Infantry) && !(ae instanceof BattleArmor))) {
            // Basic Stealth  
            switch(range) {
                case RangeType.RANGE_MINIMUM:
                case RangeType.RANGE_SHORT:
                    // At short range, basic stealth doesn't get a mod!  
                    break;
                case RangeType.RANGE_MEDIUM:
                    result = new TargetRoll(+1, "Basic Stealth Armor");
                    break;
                case RangeType.RANGE_LONG:
                case RangeType.RANGE_EXTREME:
                    // TODO : what's the *real*  
                    // modifier?  
                    result = new TargetRoll(+2, "Basic Stealth Armor");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown range constant: " + range);
            }
        } else if ((armorType == 4) && !((ae instanceof Infantry) && !(ae instanceof BattleArmor))) {
            // Prototype Stealth  
            switch(range) {
                case RangeType.RANGE_MINIMUM:
                case RangeType.RANGE_SHORT:
                    // At short range, prototype stealth doesn't get a mod!  
                    break;
                case RangeType.RANGE_MEDIUM:
                    result = new TargetRoll(+1, "Prototype Stealth Armor");
                    break;
                case RangeType.RANGE_LONG:
                case RangeType.RANGE_EXTREME:
                    // TODO : what's the *real*  
                    // modifier?  
                    result = new TargetRoll(+2, "Prototype Stealth Armor");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown range constant: " + range);
            }
        } else if ((armorType == 5) && !((ae instanceof Infantry) && !(ae instanceof BattleArmor))) {
            // Standard Stealth  
            switch(range) {
                case RangeType.RANGE_MINIMUM:
                case RangeType.RANGE_SHORT:
                    result = new TargetRoll(+1, "Standard Stealth Armor");
                    break;
                case RangeType.RANGE_MEDIUM:
                    result = new TargetRoll(+1, "Standard Stealth Armor");
                    break;
                case RangeType.RANGE_LONG:
                case RangeType.RANGE_EXTREME:
                    // TODO : what's the *real*  
                    // modifier?  
                    result = new TargetRoll(+2, "Standard Stealth Armor");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown range constant: " + range);
            }
        } else if ((armorType == 6) && !((ae instanceof Infantry) && !(ae instanceof BattleArmor))) {
            // Improved Stealth  
            switch(range) {
                case RangeType.RANGE_MINIMUM:
                case RangeType.RANGE_SHORT:
                    result = new TargetRoll(+1, "Improved Stealth Armor");
                    break;
                case RangeType.RANGE_MEDIUM:
                    result = new TargetRoll(+2, "Improved Stealth Armor");
                    break;
                case RangeType.RANGE_LONG:
                case RangeType.RANGE_EXTREME:
                    // TODO : what's the *real*  
                    // modifier?  
                    result = new TargetRoll(+3, "Improved Stealth Armor");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown range constant: " + range);
            }
        } else if (armorType == 8) {
            int mmod = 3 - delta_distance;
            mmod = Math.max(0, mmod);
            result = new TargetRoll(mmod, "mimetic armor");
        }
    } else {
        // Mimetic armor modifier is based upon the number of hexes moved,  
        // and adds to existing movement modifier (Total Warfare p228):  
        // 0 hexes moved +3 movement modifier  
        // 1 hex moved +2 movement modifier  
        // 2 hexes moved +1 movement modifier  
        // 3+ hexes moved +0 movement modifier  
        if (isMimetic) {
            int mmod = 3 - delta_distance;
            mmod = Math.max(0, mmod);
            result = new TargetRoll(mmod, "mimetic armor");
        }
        // Stealthy units alreay have their to-hit mods defined.  
        if (isStealthy && !((ae instanceof Infantry) && !(ae instanceof BattleArmor))) {
            switch(range) {
                case RangeType.RANGE_MINIMUM:
                case RangeType.RANGE_SHORT:
                    result = new TargetRoll(shortStealthMod, stealthName);
                    break;
                case RangeType.RANGE_MEDIUM:
                    result = new TargetRoll(mediumStealthMod, stealthName);
                    break;
                case RangeType.RANGE_LONG:
                case RangeType.RANGE_EXTREME:
                    // TODO : what's the *real*  
                    // modifier?  
                    result = new TargetRoll(longStealthMod, stealthName);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown range constant: " + range);
            }
        }
    }
    // Simple camo modifier is on top of the movement modifier  
    // 0 hexes moved +2 movement modifier  
    // 1 hexes moved +1 movement modifier  
    // 2+ hexes moved no modifier  
    // This can also be in addition to any armor except Mimetic!  
    if (isSimpleCamo && (delta_distance < 2)) {
        int mod = Math.max(2 - delta_distance, 0);
        if (result == null) {
            result = new TargetRoll(mod, "camoflage");
        } else {
            result.append(new TargetRoll(mod, "camoflage"));
        }
    }
    if (result == null) {
        result = new TargetRoll(0, "stealth not active");
    }
    // Return the result.  
    return result;
}
