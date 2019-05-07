/**
     * Set the troopers in the unit to the appropriate values.
     */
@Override
public void autoSetInternal() {
    // No troopers in the squad location.  
    initializeInternal(IArmorState.ARMOR_NA, LOC_SQUAD);
    // Initialize the troopers.  
    for (int loop = 1; loop < locations(); loop++) {
        initializeInternal(1, loop);
    }
    // Set the initial number of troopers that can shoot  
    // to one less than the number of locations in the unit.  
    troopersShooting = locations() - 1;
}
