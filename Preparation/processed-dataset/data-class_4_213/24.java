/** @see org.webcurator.domain.HarvestCoordinatorDAO#saveOrUpdate(BandwidthRestriction). */
public void saveOrUpdate(BandwidthRestriction aBandwidthRestriction) {
    boolean isNew = aBandwidthRestriction.getOid() == null;
    harvestCoordinatorDao.saveOrUpdate(aBandwidthRestriction);
    if (isNew) {
        auditor.audit(BandwidthRestriction.class.getName(), aBandwidthRestriction.getOid(), Auditor.ACTION_NEW_BANDWIDTH_RESTRICTION, "New bandwidth restriction: " + aBandwidthRestriction.toString());
    } else {
        auditor.audit(BandwidthRestriction.class.getName(), aBandwidthRestriction.getOid(), Auditor.ACTION_CHANGE_BANDWIDTH_RESTRICTION, "Bandwidth setting changed to: " + aBandwidthRestriction.toString());
    }
}
