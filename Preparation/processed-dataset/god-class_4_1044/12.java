/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#getQuickPickPermissions(Target)
	 */
public List<Permission> getQuickPickPermissions(Target aTarget) {
    return siteDao.getQuickPickPermissions(aTarget.getOwningUser().getAgency());
}
