/** @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#getBandwidthRestrictions(). */
public HashMap<String, List<BandwidthRestriction>> getBandwidthRestrictions() {
    return harvestCoordinatorDao.getBandwidthRestrictions();
}
