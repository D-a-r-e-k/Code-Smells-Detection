/**
	 * @see HarvestCoordinator#tailLog(TargetInstance, String, int)
	 */
public String[] tailLog(TargetInstance aTargetInstance, String aFileName, int aNoOfLines) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the tailLog command.");
    }
    String[] data = { "" };
    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING) || aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        // If we are harvesting then get the log files from the harvester  
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("Tail Log Files Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
            }
            return data;
        }
        LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
        return logReader.tail(aTargetInstance.getJobName(), aFileName, aNoOfLines);
    } else {
        // if not then check to see if the log files are available from the digital asset store.  
        LogReader logReader = digitalAssetStoreFactory.getLogReader();
        return logReader.tail(aTargetInstance.getJobName(), aFileName, aNoOfLines);
    }
}
