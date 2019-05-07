/**
	 * @see TargetManager#getAbstractTargetDTOs(String, int)
	 */
public Pagination getGroupDTOs(String name, int pageNumber, int pageSize) {
    return targetDao.getGroupDTOs(name, pageNumber, pageSize);
}
