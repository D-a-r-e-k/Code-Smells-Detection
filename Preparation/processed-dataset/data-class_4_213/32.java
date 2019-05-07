/**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#pause(TargetInstance)
     */
public void pause(TargetInstance aTargetInstance) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
    }
    HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
    if (status == null) {
        if (log.isWarnEnabled()) {
            log.warn("PAUSE Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
        }
        return;
    }
    HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
    // Update the state of the allocated Target Instance  
    aTargetInstance.setState(TargetInstance.STATE_PAUSED);
    targetInstanceDao.save(aTargetInstance);
    agent.pause(aTargetInstance.getJobName());
}
