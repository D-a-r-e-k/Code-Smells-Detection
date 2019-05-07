/**
	 * @see TargetManager#getMembers(TargetGroup, int)
	 */
public Pagination getMembers(TargetGroup aTargetGroup, int pageNum, int pageSize) {
    return targetDao.getMembers(aTargetGroup, pageNum, pageSize);
}
