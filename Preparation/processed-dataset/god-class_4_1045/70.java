public void addHarvestResources(Long harvestResultOid, Collection<HarvestResourceDTO> dtos) {
    ArcHarvestResult ahr = (ArcHarvestResult) targetInstanceDao.getHarvestResult(harvestResultOid, false);
    Collection<ArcHarvestResource> resources = new ArrayList<ArcHarvestResource>(dtos.size());
    for (HarvestResourceDTO dto : dtos) {
        resources.add(new ArcHarvestResource((ArcHarvestResourceDTO) dto, ahr));
    }
    targetInstanceDao.saveAll(resources);
}
