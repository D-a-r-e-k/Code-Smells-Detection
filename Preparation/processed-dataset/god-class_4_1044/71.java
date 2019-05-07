public List<GroupMemberDTO> getParents(final AbstractTarget aTarget) {
    return targetDao.getParents(aTarget);
}
