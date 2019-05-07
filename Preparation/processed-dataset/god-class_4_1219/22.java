/**
    * Set the troopers in the unit to the given values.
    */
public void setInternal(int value) {
    // Initialize the troopers.  
    for (int loop = 1; loop < locations(); loop++) {
        initializeInternal(value, loop);
    }
}
