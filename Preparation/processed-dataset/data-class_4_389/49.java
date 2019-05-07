/**
     * Gets the location that excess damage transfers to
     */
@Override
public HitData getTransferLocation(HitData hit) {
    return new HitData(LOC_DESTROYED);
}
