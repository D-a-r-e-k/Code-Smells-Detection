/**
	 * @see HarvestCoordinator#listLogFiles(TargetInstance)
	 */
public List<String> listLogFiles(TargetInstance aTargetInstance) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the listLogFiles command.");
    }
    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING) || aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        // If we are harvesting then get the log files from the harvester  
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("list Log Files Failed. Failed to find the Log Reader for the Job " + aTargetInstance.getJobName() + ".");
            }
            return new ArrayList<String>();
        }
        LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
        return logReader.listLogFiles(aTargetInstance.getJobName());
    } else {
        // if not then check to see if the log files are available from the digital asset store.  
        LogReader logReader = digitalAssetStoreFactory.getLogReader();
        return logReader.listLogFiles(aTargetInstance.getJobName());
    }
}
