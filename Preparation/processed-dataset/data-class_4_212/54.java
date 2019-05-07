/**
	 * @see org.webcurator.core.targets.TargetManager#isDuplicateMember(org.webcurator.domain.model.core.TargetGroup, java.lang.Long)
	 */
public boolean isDuplicateMember(TargetGroup group, Long memberOid) {
    for (GroupMemberDTO dto : group.getNewChildren()) {
        if (dto.getChildOid().equals(memberOid)) {
            return true;
        }
    }
    return targetDao.getImmediateChildrenOids(group.getOid()).contains(memberOid);
}
