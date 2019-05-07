/**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentCoordinator#harvest(TargetInstance)
     */
public void harvest(TargetInstance aTargetInstance, HarvestAgentStatusDTO aHarvestAgent) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
    }
    if (aHarvestAgent == null) {
        throw new WCTRuntimeException("A null harvest agent status was provided to the harvest command.");
    }
    // if the target is not approved to be harvested then do not harvest  
    if (queuePaused || !isTargetApproved(aTargetInstance) || aHarvestAgent.getMemoryWarning()) {
        return;
    }
    // Prepare the instance for harvesting by storing its current information.  
    prepareHarvest(aTargetInstance);
    // Run the actual harvest.  
    _harvest(aTargetInstance, aHarvestAgent);
}
