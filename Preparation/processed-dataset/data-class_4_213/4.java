/**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#reIndexHarvestResult(HarvestResult)
     */
public Boolean reIndexHarvestResult(HarvestResult origHarvestResult) {
    TargetInstance ti = origHarvestResult.getTargetInstance();
    //Assume we are already indexing  
    Boolean reIndex = false;
    try {
        reIndex = !digitalAssetStoreFactory.getDAS().checkIndexing(origHarvestResult.getOid());
    } catch (DigitalAssetStoreException ex) {
        log.error("Could not send checkIndexing message to the DAS", ex);
    }
    if (reIndex) {
        //Save any unsaved changes  
        targetInstanceDao.save(ti);
        //remove any HarvestResources and ArcHarvestFiles  
        cleanHarvestResult(origHarvestResult);
        //reload the targetInstance  
        ti = targetInstanceDao.load(ti.getOid());
        HarvestResult newHarvestResult = null;
        if (origHarvestResult instanceof ArcHarvestResult) {
            ArcHarvestResultDTO ahr = new ArcHarvestResultDTO();
            ahr.setCreationDate(new Date());
            ahr.setTargetInstanceOid(ti.getOid());
            ahr.setProvenanceNote(origHarvestResult.getProvenanceNote());
            ahr.setHarvestNumber(origHarvestResult.getHarvestNumber());
            newHarvestResult = new ArcHarvestResult(ahr, ti);
        } else {
            HarvestResultDTO hr = new HarvestResultDTO();
            hr.setCreationDate(new Date());
            hr.setTargetInstanceOid(ti.getOid());
            hr.setProvenanceNote(origHarvestResult.getProvenanceNote());
            hr.setHarvestNumber(origHarvestResult.getHarvestNumber());
            newHarvestResult = new HarvestResult(hr, ti);
        }
        origHarvestResult.setState(HarvestResult.STATE_ABORTED);
        newHarvestResult.setState(HarvestResult.STATE_INDEXING);
        List<HarvestResult> hrs = ti.getHarvestResults();
        hrs.add(newHarvestResult);
        ti.setHarvestResults(hrs);
        ti.setState(TargetInstance.STATE_HARVESTED);
        targetInstanceDao.save(newHarvestResult);
        targetInstanceDao.save(ti);
        try {
            digitalAssetStoreFactory.getDAS().initiateIndexing(new ArcHarvestResultDTO(newHarvestResult.getOid(), newHarvestResult.getTargetInstance().getOid(), newHarvestResult.getCreationDate(), newHarvestResult.getHarvestNumber(), newHarvestResult.getProvenanceNote()));
            inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_COMPLETE, ti);
            inTrayManager.generateTask(Privilege.ENDORSE_HARVEST, MessageType.TARGET_INSTANCE_ENDORSE, ti);
        } catch (DigitalAssetStoreException ex) {
            log.error("Could not send initiateIndexing message to the DAS", ex);
        }
    }
    return reIndex;
}
