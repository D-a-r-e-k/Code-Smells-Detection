/**
	 * @see TargetManager#loadGroup(Long, boolean)
	 */
public TargetGroup loadGroup(Long oid, boolean loadFully) {
    return targetDao.loadGroup(oid, loadFully);
}
