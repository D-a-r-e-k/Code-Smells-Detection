/**
	 * @see TargetManager#createGroupMemberDTO(Long, Target)
	 */
public GroupMemberDTO createGroupMemberDTO(Long groupOid, Target child) {
    TargetGroup group = targetDao.loadGroup(groupOid);
    return new GroupMemberDTO(group, child);
}
