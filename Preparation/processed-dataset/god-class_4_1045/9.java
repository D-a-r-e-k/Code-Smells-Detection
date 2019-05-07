/**
     * Internal harvest method.
     * @param aTargetInstance The instance to harvest.
     * @param aHarvestAgent The agent to harvest on.
     */
private void _harvest(TargetInstance aTargetInstance, HarvestAgentStatusDTO aHarvestAgent) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
    }
    if (aHarvestAgent == null) {
        throw new WCTRuntimeException("A null harvest agent status was provided to the harvest command.");
    }
    // if the target is not approved to be harvested then do not harvest  
    if (!isTargetApproved(aTargetInstance) || aHarvestAgent.getMemoryWarning()) {
        return;
    }
    HarvestAgent agent = harvestAgentFactory.getHarvestAgent(aHarvestAgent.getHost(), aHarvestAgent.getPort());
    // Create the seeds file contents.  
    StringBuffer seeds = new StringBuffer();
    Set<String> originalSeeds = aTargetInstance.getOriginalSeeds();
    for (String seed : originalSeeds) {
        seeds.append(seed);
        seeds.append("\n");
    }
    // Get the profile.  
    String profile = getHarvestProfileString(aTargetInstance);
    // Initiate harvest on the remote harvest agent  
    agent.initiateHarvest(aTargetInstance.getJobName(), profile, seeds.toString());
    // Update the state of the allocated Target Instance  
    aTargetInstance.setActualStartTime(new Date());
    aTargetInstance.setState(TargetInstance.STATE_RUNNING);
    aTargetInstance.setHarvestServer(aHarvestAgent.getName());
    // Save the updated information.  
    targetInstanceManager.save(aTargetInstance);
    log.info("HarvestCoordinator: Harvest initiated successfully for target instance " + aTargetInstance.getOid().toString());
    // Run the bandwidth calculations.  
    sendBandWidthRestrictions();
}
