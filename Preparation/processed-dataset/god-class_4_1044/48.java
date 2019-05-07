/**
     * @see org.webcurator.core.targets.TargetManager#countTargetGroups(org.webcurator.domain.model.auth.User)
     */
public int countTargetGroups(User aUser) {
    return targetDao.countTargetGroups(aUser.getUsername());
}
