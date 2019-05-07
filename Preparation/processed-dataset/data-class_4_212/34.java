/**
	 * @see TargetManager#createGroupMemberDTO(TargetGroup, Long)
	 */
public GroupMemberDTO createGroupMemberDTO(TargetGroup group, Long childOid) {
    AbstractTarget child = targetDao.loadAbstractTarget(childOid);
    return new GroupMemberDTO(group, child);
}
