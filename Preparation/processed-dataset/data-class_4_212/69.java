/**
	 * Deletes a pending target.
	 * @param aTarget The Target to be deleted.
	 */
public void deleteTarget(Target aTarget) {
    if (authMgr.hasPrivilege(aTarget, Privilege.DELETE_TARGET) && aTarget.getState() == Target.STATE_PENDING) {
        // Delete all links to the target(s) parents.  
        if (aTarget.getParents().size() > 0) {
            for (GroupMember parent : aTarget.getParents()) {
                parent.getParent().getRemovedChildren().add(aTarget.getOid());
                save(parent.getParent());
            }
        }
        // Delete the target itself.  
        targetDao.delete(aTarget);
    } else {
        log.error("Delete not permitted, no action taken");
    }
}
