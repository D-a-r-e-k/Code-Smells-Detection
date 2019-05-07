/** @see org.webcurator.core.harvester.agent.HarvestAgentFactory#getHarvestAgent(String, int). */
public LogReader getLogReader(String aHost, int aPort) {
    return new LogReaderSOAPClient(aHost, aPort, WCTSoapCall.AGENT_LOG_READER);
}
