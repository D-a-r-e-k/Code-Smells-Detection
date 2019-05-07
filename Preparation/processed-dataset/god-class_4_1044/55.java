/**
	 * @see org.webcurator.core.targets.TargetManager#loadAbstractTarget(java.lang.Long)
	 */
public AbstractTarget loadAbstractTarget(Long oid) {
    return targetDao.loadAbstractTarget(oid);
}
