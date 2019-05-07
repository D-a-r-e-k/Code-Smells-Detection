/**
	 * @see TargetManager#searchGroups(int, int, Long, String, String, String, String, String)	 */
public Pagination searchGroups(int pageNumber, int pageSize, Long searchOid, String name, String owner, String agency, String memberOf, String groupType, boolean nondisplayonly) {
    return targetDao.searchGroups(pageNumber, pageSize, searchOid, name, owner, agency, memberOf, groupType, nondisplayonly);
}
