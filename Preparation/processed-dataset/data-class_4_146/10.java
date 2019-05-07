/**
     * Add the given group to the list of trigger groups that will never be
     * deleted by this processor, even if a pre-processing-command to
     * delete the group is encountered.
     * 
     * @param group
     */
public void addTriggerGroupToNeverDelete(String group) {
    if (group != null)
        triggerGroupsToNeverDelete.add(group);
}
