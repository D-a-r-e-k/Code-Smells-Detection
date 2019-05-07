/**
     * Update the group status.
     * @param aTarget The target to update parents for.
     */
private void updateTargetGroupStatus(Target aTarget) {
    for (GroupMember parent : aTarget.getParents()) {
        updateTargetGroupStatus(parent.getParent());
    }
}
