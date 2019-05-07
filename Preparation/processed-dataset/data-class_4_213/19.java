/**
     * @return a harvest agent status for the specified job name
     */
protected HarvestAgentStatusDTO getHarvestAgent(String aJobName) {
    if (harvestAgents == null || harvestAgents.isEmpty()) {
        return null;
    }
    HarvestAgentStatusDTO agent = null;
    Iterator it2 = null;
    HarvesterStatusDTO hs = null;
    Iterator it = harvestAgents.values().iterator();
    while (it.hasNext()) {
        agent = (HarvestAgentStatusDTO) it.next();
        if (agent.getHarvesterStatus() != null && !agent.getHarvesterStatus().isEmpty()) {
            it2 = agent.getHarvesterStatus().values().iterator();
            while (it2.hasNext()) {
                hs = (HarvesterStatusDTO) it2.next();
                if (hs.getJobName().equals(aJobName)) {
                    return agent;
                }
            }
        }
    }
    return null;
}
