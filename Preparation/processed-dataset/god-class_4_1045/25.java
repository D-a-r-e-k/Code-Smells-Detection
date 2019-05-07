/** @see org.webcurator.domain.HarvestCoordinatorDAO#delete(Object). */
public void delete(BandwidthRestriction aBandwidthRestriction) {
    auditor.audit(BandwidthRestriction.class.getName(), null, Auditor.ACTION_DELETE_BANDWIDTH_RESTRICTION, "Deleted bandwidth restriction: " + aBandwidthRestriction.toString());
    harvestCoordinatorDao.delete(aBandwidthRestriction);
}
