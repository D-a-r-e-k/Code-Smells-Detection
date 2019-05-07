/**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#stop(TargetInstance)
     */
public void stop(TargetInstance aTargetInstance) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
    }
    HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
    if (status == null) {
        if (log.isWarnEnabled()) {
            log.warn("STOP Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
        }
        return;
    }
    HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
    agent.stop(aTargetInstance.getJobName());
}
