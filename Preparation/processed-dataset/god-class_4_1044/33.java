/**
	 * @see TargetManager#getAbstractTargetDTOs(String, int)
	 */
public Pagination getNonSubGroupDTOs(String name, int pageNumber, int pageSize) {
    return targetDao.getNonSubGroupDTOs(name, subGroupTypeName, pageNumber, pageSize);
}
