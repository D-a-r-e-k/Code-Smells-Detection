/**
	 * @see org.webcurator.core.targets.TargetManager#getAncestorOids(org.webcurator.domain.model.core.AbstractTarget)
	 */
public Set<Long> getAncestorOids(AbstractTarget child) {
    return targetDao.getAncestorOids(child.getOid());
}
