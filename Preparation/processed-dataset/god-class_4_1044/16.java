/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#loadPermission(java.lang.Long)
	 */
public Permission loadPermission(Long oid) {
    return siteDao.loadPermission(oid);
}
