private void cleanHarvestResult(HarvestResult harvestResult) {
    if (harvestResult != null) {
        if (harvestResult.getResources() != null) {
            targetInstanceDao.deleteHarvestResultResources(harvestResult.getOid());
        }
        if (harvestResult instanceof ArcHarvestResult) {
            targetInstanceDao.deleteHarvestResultFiles(harvestResult.getOid());
        }
    }
}
