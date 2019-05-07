/**
     * Battle Armor units don't transfer damage.
     */
@Override
public HitData getTransferLocation(HitData hit) {
    // If any trooper lives, the unit isn't destroyed.  
    for (int loop = 1; loop < locations(); loop++) {
        if (0 < this.getInternal(loop)) {
            return new HitData(Entity.LOC_NONE);
        }
    }
    // No surviving troopers, so we're toast.  
    return new HitData(Entity.LOC_DESTROYED);
}
