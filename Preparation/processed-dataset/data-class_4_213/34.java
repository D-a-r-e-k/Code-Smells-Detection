/**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#abort(TargetInstance)
     */
public void abort(TargetInstance aTargetInstance) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
    }
    // Update the state of the allocated Target Instance  
    aTargetInstance.setState(TargetInstance.STATE_ABORTED);
    targetInstanceDao.save(aTargetInstance);
    HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
    if (status == null) {
        if (log.isWarnEnabled()) {
            log.warn("ABORT Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
        }
    } else {
        HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
        try {
            agent.abort(aTargetInstance.getJobName());
        } catch (RuntimeException e) {
            if (log.isWarnEnabled()) {
                log.warn("ABORT Failed. Failed Abort the Job " + aTargetInstance.getJobName() + " on the Harvest Agent " + agent.getName() + ".");
            }
        }
    }
    sendBandWidthRestrictions();
}
