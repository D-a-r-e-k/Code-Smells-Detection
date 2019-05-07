/**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#harvestComplete(org.webcurator.core.model.HarvestResult)
     */
public void harvestComplete(HarvestResultDTO aResult) {
    TargetInstance ti = targetInstanceDao.load(aResult.getTargetInstanceOid());
    if (ti == null) {
        throw new WCTRuntimeException("Unknown TargetInstance oid recieved " + aResult.getTargetInstanceOid() + " failed to save HarvestResult.");
    }
    //The result is for the original harvest, but the TI already has one or more results  
    if (aResult.getHarvestNumber() == 1 && !ti.getHarvestResults().isEmpty()) {
        //This is a repeat message probably due to a timeout. Leaving this to run  
        //would generate a second 'Original Harvest' which will subsequently fail in indexing   
        //due to a duplicate file name constraint in the arc_harvest_file table  
        log.warn("Duplicate 'Harvest Complete' message received for job: " + ti.getOid() + ". Message ignored.");
        return;
    }
    log.info("'Harvest Complete' message received for job: " + ti.getOid() + ".");
    HarvestResult harvestResult = null;
    if (aResult instanceof ArcHarvestResultDTO) {
        harvestResult = new ArcHarvestResult((ArcHarvestResultDTO) aResult, ti);
    } else {
        harvestResult = new HarvestResult(aResult, ti);
    }
    harvestResult.setState(HarvestResult.STATE_INDEXING);
    List<HarvestResult> hrs = ti.getHarvestResults();
    hrs.add(harvestResult);
    ti.setHarvestResults(hrs);
    ti.setState(TargetInstance.STATE_HARVESTED);
    targetInstanceDao.save(harvestResult);
    targetInstanceDao.save(ti);
    sendBandWidthRestrictions();
    // IF the associated target record for this TI has  
    // no active TIs remaining (scheduled, queued, running,   
    // paused, stopping)  
    // AND  
    // the target's schedule is not active (i.e we're past  
    // the schedule end date),  
    // THEN set the status of the associated target to 'complete'.  
    //  
    boolean bActiveSchedules = false;
    for (Schedule schedule : ti.getTarget().getSchedules()) {
        if (schedule.getEndDate() == null) {
            bActiveSchedules = true;
        } else if (schedule.getEndDate().after(new Date())) {
            bActiveSchedules = true;
        }
    }
    if (targetInstanceDao.countActiveTIsForTarget(ti.getTarget().getOid()) == 0 && !bActiveSchedules) {
        Target t = targetManager.load(ti.getTarget().getOid(), true);
        t.changeState(Target.STATE_COMPLETED);
        targetManager.save(t);
    }
    // Ask the DigitalAssetStore to index the ARC  
    try {
        digitalAssetStoreFactory.getDAS().initiateIndexing(new ArcHarvestResultDTO(harvestResult.getOid(), harvestResult.getTargetInstance().getOid(), harvestResult.getCreationDate(), harvestResult.getHarvestNumber(), harvestResult.getProvenanceNote()));
    } catch (DigitalAssetStoreException ex) {
        log.error("Could not send initiateIndexing message to the DAS", ex);
    }
    inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_COMPLETE, ti);
    inTrayManager.generateTask(Privilege.ENDORSE_HARVEST, MessageType.TARGET_INSTANCE_ENDORSE, ti);
    log.info("'Harvest Complete' message processed for job: " + ti.getOid() + ".");
}
