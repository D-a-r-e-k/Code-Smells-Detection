/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#getNextStates(org.webcurator.domain.model.core.Target)
	 */
public int[] getNextStates(Target aTarget) {
    switch(aTarget.getOriginalState()) {
        case Target.STATE_PENDING:
            return authMgr.hasPrivilege(aTarget, Privilege.APPROVE_TARGET) ? new int[] { Target.STATE_NOMINATED, Target.STATE_APPROVED, Target.STATE_REJECTED } : new int[] { Target.STATE_NOMINATED };
        case Target.STATE_REINSTATED:
            return authMgr.hasPrivilege(aTarget, Privilege.APPROVE_TARGET) ? new int[] { Target.STATE_NOMINATED, Target.STATE_APPROVED, Target.STATE_REJECTED } : new int[] { Target.STATE_NOMINATED };
        case Target.STATE_NOMINATED:
            return authMgr.hasPrivilege(aTarget, Privilege.APPROVE_TARGET) ? new int[] { Target.STATE_APPROVED, Target.STATE_REJECTED } : new int[] {};
        case Target.STATE_REJECTED:
            return authMgr.hasPrivilege(aTarget, Privilege.REINSTATE_TARGET) ? new int[] { Target.STATE_REINSTATED } : new int[] {};
        case Target.STATE_APPROVED:
            return authMgr.hasPrivilege(aTarget, Privilege.CANCEL_TARGET) ? new int[] { Target.STATE_CANCELLED } : new int[] {};
        case Target.STATE_CANCELLED:
            return authMgr.hasPrivilege(aTarget, Privilege.REINSTATE_TARGET) ? new int[] { Target.STATE_REINSTATED } : new int[] {};
        case Target.STATE_COMPLETED:
            return authMgr.hasPrivilege(aTarget, Privilege.REINSTATE_TARGET) ? new int[] { Target.STATE_REINSTATED } : new int[] {};
        default:
            assert false : "Illegal State";
            return new int[] {};
    }
}
