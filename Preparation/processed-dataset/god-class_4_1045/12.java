/**
     * Send the bandwidth restrictions to all the harvest jobs in the 
     * running or paused states.   
     */
private void sendBandWidthRestrictions() {
    HarvestAgent agent = null;
    HarvestAgentStatusDTO ha = null;
    TargetInstance ti = null;
    // Allocate the bandwidth  
    HashMap<Long, TargetInstance> running = calculateBandwidthAllocation();
    Iterator it = running.values().iterator();
    while (it.hasNext()) {
        ti = (TargetInstance) it.next();
        ha = getHarvestAgent(ti.getJobName());
        if (ha != null) {
            agent = harvestAgentFactory.getHarvestAgent(ha.getHost(), ha.getPort());
            if (ti.getAllocatedBandwidth() == null || ti.getAllocatedBandwidth().intValue() <= 0) {
                // if we get to this point and the bandwidth is set to zero then set it to be one  
                // zero allows the harvester to use as much bandwidth as it likes.  
                ti.setAllocatedBandwidth(new Long(1));
            }
            agent.restrictBandwidth(ti.getJobName(), ti.getAllocatedBandwidth().intValue());
            targetInstanceDao.save(ti);
        }
    }
}
