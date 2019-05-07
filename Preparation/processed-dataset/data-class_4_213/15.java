/** @see HarvestCoordinator#calculateBandwidthAllocation(TargetInstance). */
private HashMap<Long, TargetInstance> calculateBandwidthAllocation(List<TargetInstance> aRunningTargetInstances) {
    HashMap<Long, TargetInstance> results = new HashMap<Long, TargetInstance>();
    // Get the global max bandwidth setting for the current period.  
    long maxBandwidth = getCurrentGlobalMaxBandwidth();
    BandwidthCalculator.calculateBandwidthAllocation(aRunningTargetInstances, maxBandwidth, maxBandwidthPercent);
    return results;
}
