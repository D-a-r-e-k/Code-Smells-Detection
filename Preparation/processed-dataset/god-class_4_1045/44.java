/**
	 * Return the next harvest agent to allocate a target instance to.
	 * @param aTargetInstance the target instance to assign
	 * @return the harvest agent
	 */
private HarvestAgentStatusDTO getHarvester(String aAgencyName) {
    HarvestAgentStatusDTO selectedAgent = null;
    HarvestAgentStatusDTO agent = null;
    Iterator<HarvestAgentStatusDTO> it = harvestAgents.values().iterator();
    while (it.hasNext()) {
        agent = (HarvestAgentStatusDTO) it.next();
        if (agent.getAllowedAgencies() == null || agent.getAllowedAgencies().isEmpty() || agent.getAllowedAgencies().contains(aAgencyName)) {
            if (selectedAgent == null || agent.getHarvesterStatusCount() < selectedAgent.getHarvesterStatusCount()) {
                if (!agent.getMemoryWarning() && !agent.isInTransition() && agent.getHarvesterStatusCount() < agent.getMaxHarvests()) {
                    selectedAgent = agent;
                }
            }
        }
    }
    return selectedAgent;
}
