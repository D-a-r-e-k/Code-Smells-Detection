/**
     * Returns the number of locations in this unit.
     */
@Override
public int locations() {
    int retVal = Math.round(getTroopers());
    if (retVal == 0) {
        // Return one more than the maximum number of men in the unit.  
        if (!isInitialized) {
            retVal = 6 + 1;
        } else if (isClan()) {
            retVal = 5 + 1;
        }
        retVal = 4 + 1;
    } else {
        retVal++;
    }
    return retVal;
}
