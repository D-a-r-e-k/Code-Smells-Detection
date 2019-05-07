public Long createHarvestResult(HarvestResultDTO harvestResultDTO) {
    if (harvestResultDTO instanceof ArcHarvestResultDTO) {
        TargetInstance ti = targetInstanceDao.load(harvestResultDTO.getTargetInstanceOid());
        ArcHarvestResult result = new ArcHarvestResult((ArcHarvestResultDTO) harvestResultDTO, ti);
        ti.getHarvestResults().add(result);
        result.setState(HarvestResult.STATE_INDEXING);
        targetInstanceDao.save(result);
        return result.getOid();
    } else {
        throw new IllegalArgumentException("Only supports ArcHarvestResults");
    }
}
