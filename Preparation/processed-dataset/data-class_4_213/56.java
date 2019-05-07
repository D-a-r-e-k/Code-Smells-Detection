/**
	 * @see HarvestCoordinator#getLogLinesByRegex(TargetInstance, String, int, String)
	 */
public String[] getLogLinesByRegex(TargetInstance aTargetInstance, String aFileName, int aNoOfLines, String aRegex, boolean prependLineNumbers) {
    if (aTargetInstance == null) {
        throw new WCTRuntimeException("A null target instance was provided to the regex command.");
    }
    String[] data = { "" };
    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING) || aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        // If we are harvesting then get the log files from the harvester  
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("Get log lines by regex failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
            }
            return data;
        }
        LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
        return logReader.getByRegExpr(aTargetInstance.getJobName(), aFileName, aRegex, "zzzzzzzzz", prependLineNumbers, 0, aNoOfLines);
    } else {
        // if not then check to see if the log files are available from the digital asset store.  
        LogReader logReader = digitalAssetStoreFactory.getLogReader();
        return logReader.getByRegExpr(aTargetInstance.getJobName(), aFileName, aRegex, "zzzzzzzz", prependLineNumbers, 0, aNoOfLines);
    }
}
