/**
     * Determine if the unit can be repaired, or only harvested for spares.
     * 
     * @return A <code>boolean</code> that is <code>true</code> if the unit can
     *         be repaired (given enough time and parts); if this value is
     *         <code>false</code>, the unit is only a source of spares.
     * @see Entity#isSalvage()
     */
@Override
public boolean isRepairable() {
    // A tank is repairable if it is salvageable,  
    // and none of its body internals are gone.  
    boolean retval = isSalvage();
    int loc = Tank.LOC_FRONT;
    while (retval && (loc < Tank.LOC_TURRET)) {
        int loc_is = this.getInternal(loc);
        loc++;
        retval = (loc_is != IArmorState.ARMOR_DOOMED) && (loc_is != IArmorState.ARMOR_DESTROYED);
    }
    return retval;
}
