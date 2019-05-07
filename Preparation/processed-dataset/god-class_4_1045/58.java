/** 
     * @see HarvestCoordinator#purgeDigitalAssets().
     */
public void purgeDigitalAssets() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, daysBeforeDASPurge * -1);
    List<TargetInstance> tis = targetInstanceDao.findPurgeableTargetInstances(cal.getTime());
    if (log.isDebugEnabled()) {
        log.debug("Attempting to purge " + tis.size() + " harvests from the digital asset store.");
    }
    if (tis != null && !tis.isEmpty()) {
        int index = 0;
        String[] tiNames = new String[tis.size()];
        for (TargetInstance ti : tis) {
            tiNames[index++] = ti.getJobName();
        }
        try {
            digitalAssetStoreFactory.getDAS().purge(tiNames);
            for (TargetInstance ti : tis) {
                targetInstanceManager.purgeTargetInstance(ti);
            }
        } catch (DigitalAssetStoreException e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to complete the purge " + e.getMessage(), e);
            }
        }
    }
}
