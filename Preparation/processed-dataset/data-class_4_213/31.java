/**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#updateProfileOverrides(TargetInstance)
     */
public void updateProfileOverrides(TargetInstance aTargetInstance) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided.");
    }
    HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
    if (status == null) {
        if (log.isWarnEnabled()) {
            log.warn("Update Profile Overrides Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
        }
        return;
    }
    String profile = getHarvestProfileString(aTargetInstance);
    HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
    agent.updateProfileOverrides(aTargetInstance.getJobName(), profile);
}
