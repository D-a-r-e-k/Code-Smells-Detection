/**
	 * Check to see that at least the minimum amount of bandwith can be allocated to
	 * all the running target instances assuming that this target instance is 
	 * allocated to a harvest agent
	 * @param aTargetInstance the target instances that may be allocated
	 * @return true if the minimum bandwidth will be available.
	 */
private boolean isMiniumBandwidthAvailable(QueuedTargetInstanceDTO aTargetInstance) {
    if (null == aTargetInstance) {
        throw new WCTRuntimeException("The Target Instance passed in was null.");
    }
    if (getCurrentGlobalMaxBandwidth() < minimumBandwidth) {
        return false;
    }
    TargetInstance ti = targetInstanceDao.load(aTargetInstance.getOid());
    ti = targetInstanceDao.populate(ti);
    return isMiniumBandwidthAvailable(ti);
}
