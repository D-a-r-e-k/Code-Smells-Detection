/**
	 * @see HarvestCoordinator#countLogLines(TargetInstance, String)
	 */
public Integer countLogLines(TargetInstance aTargetInstance, String aFileName) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the countLogLines command.");
    }
    Integer count = 0;
    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING) || aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        // If we are harvesting then get the log files from the harvester  
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("Count Log Lines Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
            }
            return count;
        }
        LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
        return logReader.countLines(aTargetInstance.getJobName(), aFileName);
    } else {
        // if not then check to see if the log files are available from the digital asset store.  
        LogReader logReader = digitalAssetStoreFactory.getLogReader();
        return logReader.countLines(aTargetInstance.getJobName(), aFileName);
    }
}
