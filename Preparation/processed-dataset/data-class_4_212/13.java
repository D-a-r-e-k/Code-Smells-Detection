/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#findPermissionsBySiteTitle(java.lang.String, int)
	 */
public Pagination findPermissionsBySiteTitle(Target aTarget, String aSiteTitle, int aPageNumber) {
    return siteDao.findPermissionsBySiteTitle(aTarget.getOwningUser().getAgency().getOid(), aSiteTitle, aPageNumber);
}
