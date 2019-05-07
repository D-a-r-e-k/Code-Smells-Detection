public void removeIndexes(HarvestResult hr) {
    DigitalAssetStore das = digitalAssetStoreFactory.getDAS();
    try {
        log.info("Attempting to remove indexes for TargetInstance " + hr.getTargetInstance().getOid() + " HarvestNumber " + hr.getHarvestNumber());
        das.initiateRemoveIndexes(new ArcHarvestResultDTO(hr.getOid(), hr.getTargetInstance().getOid(), hr.getCreationDate(), hr.getHarvestNumber(), hr.getProvenanceNote()));
    } catch (DigitalAssetStoreException e) {
        if (log.isErrorEnabled()) {
            log.error("Could not send initiateRemoveIndexes message to the DAS for TargetInstance " + hr.getTargetInstance().getOid() + " HarvestNumber " + hr.getHarvestNumber() + ": " + e.getMessage(), e);
        }
    }
}
