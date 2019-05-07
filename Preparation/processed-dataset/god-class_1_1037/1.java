/** @see org.webcurator.core.harvester.agent.HarvestAgentFactory#getHarvestAgent(String, int). */
public HarvestAgent getHarvestAgent(String aHost, int aPort) {
    HarvestAgentSOAPClient ha = new HarvestAgentSOAPClient();
    ha.setHost(aHost);
    ha.setPort(aPort);
    return ha;
}
