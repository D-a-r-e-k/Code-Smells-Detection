/**
     * Try's to reset the super class and reset this class for 
     * re-use, so that you don't need to create a new serializer 
     * (mostly for performance reasons).
     * 
     * @return true if the class was successfuly reset.
     */
public boolean reset() {
    boolean wasReset = false;
    if (super.reset()) {
        resetToStream();
        wasReset = true;
    }
    return wasReset;
}
