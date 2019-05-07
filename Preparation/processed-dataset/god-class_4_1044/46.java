/**
     * Get the seeds for a Target that belong within a given agency.
     * @param aTarget The target to get the seeds for.
     * @param agencyOid The agency to which to restrict the recursion.
     */
public Set<Seed> getSeeds(AbstractTarget aTarget, Long agencyOid) {
    int objectType = aTarget.getObjectType();
    if (objectType == AbstractTarget.TYPE_TARGET) {
        Target theTarget = null;
        if (aTarget instanceof Target) {
            theTarget = (Target) aTarget;
        } else {
            //Force a reload of the Target to overcome lazy load ClassCastException  
            theTarget = targetDao.load(aTarget.getOid());
        }
        return targetDao.getSeeds(theTarget);
    } else if (objectType == AbstractTarget.TYPE_GROUP) {
        TargetGroup theTargetGroup = null;
        if (aTarget instanceof TargetGroup) {
            theTargetGroup = (TargetGroup) aTarget;
        } else {
            //Force a reload of the TargetGroup to overcome lazy load ClassCastException  
            theTargetGroup = targetDao.loadGroup(aTarget.getOid());
        }
        return targetDao.getSeeds(theTargetGroup, agencyOid, subGroupTypeName);
    } else {
        throw new WCTRuntimeException("Unknown Target Type for getSeeds: " + objectType);
    }
}
