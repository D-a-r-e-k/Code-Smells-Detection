/** @see HarvestCoordinator#calculateBandwidthAllocation(TargetInstance). */
private HashMap<Long, TargetInstance> calculateBandwidthAllocation() {
    //  Check to see if there are other running target instances with a percentage allocation.  
    TargetInstanceCriteria tic = new TargetInstanceCriteria();
    Set<String> states = new HashSet<String>();
    states.add(TargetInstance.STATE_RUNNING);
    states.add(TargetInstance.STATE_PAUSED);
    tic.setStates(states);
    List<TargetInstance> runningTIs = targetInstanceDao.findTargetInstances(tic);
    return calculateBandwidthAllocation(runningTIs);
}
