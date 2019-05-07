/** @see org.webcurator.domain.HarvestCoordinatorDAO#getBandwidthRestriction(String, Date). */
public BandwidthRestriction getBandwidthRestriction(String aDay, Date aTime) {
    return harvestCoordinatorDao.getBandwidthRestriction(aDay, aTime);
}
