/**
     * Determine if this unit has an active and working stealth system. (stealth
     * can be active and not working when under ECCM)
     * <p/>
     * Sub-classes are encouraged to override this method.
     * 
     * @return <code>true</code> if this unit has a stealth system that is
     *         currently active, <code>false</code> if there is no stealth
     *         system or if it is inactive.
     */
@Override
public boolean isStealthOn() {
    // Try to find a Mek Stealth system.  
    for (Mounted mEquip : getMisc()) {
        MiscType mtype = (MiscType) mEquip.getType();
        if (mtype.hasFlag(MiscType.F_STEALTH)) {
            if (mEquip.curMode().equals("On")) {
                // Return true if the mode is "On"  
                return true;
            }
        }
    }
    // No Mek Stealth or system inactive. Return false.  
    return false;
}
