/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#findPermissionsByUrl(java.lang.String, int)
	 */
public Pagination findPermissionsByUrl(Target aTarget, String aUrl, int aPageNumber) {
    List<Permission> permissionList = new LinkedList<Permission>();
    permissionList.addAll(PermissionMappingStrategy.getStrategy().getMatchingPermissions(aTarget, aUrl));
    return new Pagination(permissionList, aPageNumber, Constants.GBL_PAGE_SIZE);
}
