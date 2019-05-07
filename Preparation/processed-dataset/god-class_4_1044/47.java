/**
     * @see org.webcurator.core.targets.TargetManager#countTargets(org.webcurator.domain.model.auth.User)
     */
public int countTargets(User aUser) {
    return targetDao.countTargets(aUser.getUsername());
}
