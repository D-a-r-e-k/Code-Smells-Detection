/** @see HarvestCoordinator#isMiniumBandwidthAvailable(TargetInstance) . */
public boolean isMiniumBandwidthAvailable(TargetInstance aTargetInstance) {
    if (null == aTargetInstance) {
        throw new WCTRuntimeException("The Target Instance passed in was null.");
    }
    if (getCurrentGlobalMaxBandwidth() < minimumBandwidth) {
        return false;
    }
    TargetInstance ti = targetInstanceDao.load(aTargetInstance.getOid());
    ti = targetInstanceDao.populate(ti);
    HashMap targetInstances = null;
    if (TargetInstance.STATE_PAUSED.equals(ti.getState())) {
        targetInstances = calculateBandwidthAllocation();
    } else {
        targetInstances = calculateBandwidthAllocation(ti);
    }
    if (ti.getBandwidthPercent() == null) {
        if (ti.getAllocatedBandwidth() < minimumBandwidth) {
            // failure bandwidth setting is too low.                      
            return false;
        }
    } else {
        TargetInstance ati = null;
        Iterator it = targetInstances.values().iterator();
        while (it.hasNext()) {
            ati = (TargetInstance) it.next();
            if (ati.getBandwidthPercent() == null) {
                if (ati.getAllocatedBandwidth() < minimumBandwidth) {
                    // failure bandwidth setting is too low.                              
                    return false;
                }
            }
        }
    }
    return true;
}
