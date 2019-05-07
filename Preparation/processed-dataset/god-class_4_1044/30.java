/**
	 * @see TargetManager#getAbstractTargetDTOs(String, int)
	 */
public Pagination getAbstractTargetDTOs(String name, int pageNumber, int pageSize) {
    return targetDao.getAbstractTargetDTOs(name, pageNumber, pageSize);
}
