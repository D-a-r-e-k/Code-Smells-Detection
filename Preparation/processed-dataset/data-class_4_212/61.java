/** 
	 * Retrieve a List of active parent groups.
	 * @param aTargetInstance The target instance to find the parent groups for.
	 * @return A List of active parent groups.
	 */
public List<TargetGroup> getActiveParentGroups(TargetInstance aTargetInstance) {
    List<TargetGroup> ancestorList = new LinkedList<TargetGroup>();
    for (GroupMember parents : aTargetInstance.getTarget().getParents()) {
        getActiveParentGroups(parents.getParent(), aTargetInstance.getActualStartTime(), ancestorList);
    }
    return ancestorList;
}
