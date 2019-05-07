/**
     * OmniVehicles have handles for Battle Armor squads to latch onto. Please
     * note, this method should only be called during this Tank's construction.
     * <p/>
     * Overrides <code>Entity#setOmni(boolean)</code>
     */
@Override
public void setOmni(boolean omni) {
    // Perform the superclass' action.  
    super.setOmni(omni);
    // Add BattleArmorHandles to OmniMechs.  
    if (omni && !hasBattleArmorHandles()) {
        addTransporter(new BattleArmorHandlesTank());
    }
}
