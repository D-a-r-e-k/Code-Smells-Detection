public void finaliseIndex(Long harvestResultOid) {
    ArcHarvestResult ahr = (ArcHarvestResult) targetInstanceDao.getHarvestResult(harvestResultOid, false);
    ahr.setState(0);
    triggerAutoQA(ahr);
    targetInstanceDao.save(ahr);
}
