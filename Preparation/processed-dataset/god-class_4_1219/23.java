/**
     * Mounts the specified equipment in the specified location.
     */
@Override
protected void addEquipment(Mounted mounted, int loc, boolean rearMounted) throws LocationFullException {
    // Implement parent's behavior.  
    super.addEquipment(mounted, loc, rearMounted);
    // Add the piece equipment to our slots.  
    addCritical(loc, new CriticalSlot(CriticalSlot.TYPE_EQUIPMENT, getEquipmentNum(mounted), true, mounted));
    // Is the item a stealth equipment?  
    // TODO: what's the *real* extreme range modifier?  
    String name = mounted.getType().getInternalName();
    if (BattleArmor.STEALTH.equals(name)) {
        isStealthy = true;
        shortStealthMod = 0;
        mediumStealthMod = 1;
        longStealthMod = 2;
        stealthName = name;
    } else if (BattleArmor.ADVANCED_STEALTH.equals(name)) {
        isStealthy = true;
        shortStealthMod = 1;
        mediumStealthMod = 1;
        longStealthMod = 2;
        stealthName = name;
    } else if (BattleArmor.EXPERT_STEALTH.equals(name)) {
        isStealthy = true;
        shortStealthMod = 1;
        mediumStealthMod = 2;
        longStealthMod = 3;
        stealthName = name;
    } else if (BattleArmor.MIMETIC_CAMO.equals(name)) {
        isMimetic = true;
        shortStealthMod = 3;
        mediumStealthMod = 2;
        longStealthMod = 1;
        stealthName = name;
    } else if (BattleArmor.SIMPLE_CAMO.equals(name)) {
        isSimpleCamo = true;
        shortStealthMod = 2;
        mediumStealthMod = 1;
        longStealthMod = 0;
        stealthName = name;
    }
    // If the BA can swarm, they're anti-mek.  
    if (Infantry.SWARM_MEK.equals(name)) {
        setAntiMek(true);
    }
}
