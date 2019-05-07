/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#load(java.lang.Long, boolean)
	 */
public Target load(Long oid, boolean loadFully) {
    return targetDao.load(oid, loadFully);
}
