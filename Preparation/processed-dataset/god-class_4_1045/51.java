/**
	 * @see HarvestCoordinator#headLog(TargetInstance, String, int)
	 */
public String[] headLog(TargetInstance aTargetInstance, String aFileName, int aNoOfLines) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the headLog command.");
    }
    String[] data = { "" };
    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING) || aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        // If we are harvesting then get the log files from the harvester  
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("Head Log Files Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
            }
            return data;
        }
        LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
        return logReader.get(aTargetInstance.getJobName(), aFileName, 1, aNoOfLines);
    } else {
        // if not then check to see if the log files are available from the digital asset store.  
        LogReader logReader = digitalAssetStoreFactory.getLogReader();
        return logReader.get(aTargetInstance.getJobName(), aFileName, 1, aNoOfLines);
    }
}
