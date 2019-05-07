/**
     * Remove the given group to the list of trigger groups that will never be
     * deleted by this processor, even if a pre-processing-command to
     * delete the group is encountered.
     * 
     * @param group
     */
public boolean removeTriggerGroupToNeverDelete(String group) {
    if (group != null)
        return triggerGroupsToNeverDelete.remove(group);
    return false;
}
