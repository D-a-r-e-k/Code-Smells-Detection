/**
	 * @see org.webcurator.core.targets.TargetManager#allowStateChange(org.webcurator.domain.model.core.Target, int)
	 */
public boolean allowStateChange(Target aTarget, int nextState) {
    if (aTarget.getOriginalState() == nextState) {
        return true;
    } else {
        int[] nextStates = getNextStates(aTarget);
        for (int i = 0; i < nextStates.length; i++) {
            if (nextStates[i] == nextState) {
                return true;
            }
        }
        return false;
    }
}
