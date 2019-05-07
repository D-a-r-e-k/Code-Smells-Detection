/**
	 * Private method to recurse through groups and find the active parents.
	 * @param aGroup The parent to start from
	 * @param aDate  The date at which the group must be valid.
	 * @param destList The list to add the parents to.
	 */
private void getActiveParentGroups(TargetGroup aGroup, Date aDate, List<TargetGroup> destList) {
    // Check that the group is active.  
    if ((aGroup.getFromDate() == null || aGroup.getFromDate().before(aDate)) && (aGroup.getToDate() == null || aGroup.getToDate().after(aDate))) {
        destList.add(aGroup);
        for (GroupMember parents : aGroup.getParents()) {
            getActiveParentGroups(parents.getParent(), aDate, destList);
        }
    }
}
