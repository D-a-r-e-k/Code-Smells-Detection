/**
	 * @see HarvestCoordinator#getFirstLogLineBeginning(TargetInstance, String, String)
	 */
public Integer getFirstLogLineBeginning(TargetInstance aTargetInstance, String aFileName, String match) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the getFirstLogLineBeginning command.");
    }
    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING) || aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        // If we are harvesting then get the log files from the harvester  
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("Get First Log Line Beginning failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
            }
            return new Integer(0);
        }
        LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
        return logReader.findFirstLineBeginning(aTargetInstance.getJobName(), aFileName, match);
    } else {
        // if not then check to see if the log files are available from the digital asset store.  
        LogReader logReader = digitalAssetStoreFactory.getLogReader();
        return logReader.findFirstLineBeginning(aTargetInstance.getJobName(), aFileName, match);
    }
}
