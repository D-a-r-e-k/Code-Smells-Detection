public void addToHarvestResult(Long harvestResultOid, ArcHarvestFileDTO ahf) {
    ArcHarvestResult ahr = (ArcHarvestResult) targetInstanceDao.getHarvestResult(harvestResultOid, false);
    ArcHarvestFile f = new ArcHarvestFile(ahf, ahr);
    targetInstanceDao.save(f);
}
