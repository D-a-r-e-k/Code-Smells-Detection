/**
	 * Deletes a TargetGroup by OID.
	 * @param aTargetGroup The group to be deleted.
	 * @return true if the target group was deleted.
	 */
public boolean deleteTargetGroup(TargetGroup aTargetGroup) {
    if (authMgr.hasPrivilege(aTargetGroup, Privilege.MANAGE_GROUP)) {
        // Delete the target itself.  
        return targetDao.deleteGroup(aTargetGroup);
    } else {
        log.error("Delete not permitted, no action taken");
        throw new WCTRuntimeException("You do not have the appropriate privileges to delete this group");
    }
}
