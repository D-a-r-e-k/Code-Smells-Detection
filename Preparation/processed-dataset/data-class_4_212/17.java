/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#isNameOk(org.webcurator.domain.model.core.Target)
	 */
public boolean isNameOk(AbstractTarget aTarget) {
    return targetDao.isNameOk(aTarget);
}
