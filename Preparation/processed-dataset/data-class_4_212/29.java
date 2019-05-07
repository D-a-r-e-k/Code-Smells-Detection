/**
	 * @see TargetManager#getParents(AbstractTarget, int)
	 */
public Pagination getParents(AbstractTarget aTarget, int pageNum, int pageSize) {
    return targetDao.getParents(aTarget, pageNum, pageSize);
}
