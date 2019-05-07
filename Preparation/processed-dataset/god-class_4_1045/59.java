/** 
     * @see HarvestCoordinator#purgeAbortedTargetInstances().
     */
public void purgeAbortedTargetInstances() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, daysBeforeAbortedTargetInstancePurge * -1);
    List<TargetInstance> tis = targetInstanceDao.findPurgeableAbortedTargetInstances(cal.getTime());
    if (log.isDebugEnabled()) {
        log.debug("Attempting to purge " + tis.size() + " aborted harvests from the system.");
    }
    if (tis != null && !tis.isEmpty()) {
        int index = 0;
        String[] tiNames = new String[tis.size()];
        for (TargetInstance ti : tis) {
            tiNames[index++] = ti.getJobName();
        }
        HarvestAgentSOAPClient ha = new HarvestAgentSOAPClient();
        ha.setHost(harvestAgentConfig.getHost());
        ha.setPort(harvestAgentConfig.getPort());
        ha.setService(harvestAgentConfig.getHarvestAgentServiceName());
        try {
            ha.purgeAbortedTargetInstances(tiNames);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to complete the purge of aborted ti data via HA: " + e.getMessage(), e);
            }
        }
        // call the same web-method on the DAS, to delete folders which  
        // may have been created in error while a running harvest was in  
        // transition from running to stopping to harvested.  
        try {
            digitalAssetStoreFactory.getDAS().purgeAbortedTargetInstances(tiNames);
        } catch (DigitalAssetStoreException e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to complete the purge of aborted ti data via DAS: " + e.getMessage(), e);
            }
        }
        try {
            for (TargetInstance ti : tis) {
                targetInstanceManager.purgeTargetInstance(ti);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to set the purged flag on all of the eligible aborted TIs: " + e.getMessage(), e);
            }
        }
    }
}
