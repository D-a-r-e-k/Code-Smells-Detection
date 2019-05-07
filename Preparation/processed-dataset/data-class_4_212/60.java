/**
	 * Search the Permissions.
	 * @param aPermissionCriteria The criteria to use to search the permissions.
	 * @return A Pagination of permission records.
	 */
public Pagination searchPermissions(PermissionCriteria aPermissionCriteria) {
    return targetDao.searchPermissions(aPermissionCriteria);
}
