/**
     * Grabs control over this PoolElement and returns true on success
     * @return true if the control over this PoolElement was grabbed successfully
     */
public synchronized short grab() {
    if (this.isActive)
        return ACTIVE;
    if (!isValid())
        return INVALID;
    this.isActive = true;
    this.hasBeenUsed = true;
    return IDLE;
}
