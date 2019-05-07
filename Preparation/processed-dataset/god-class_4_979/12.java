/**
     * Get the (unmodifiable) list of trigger groups that will never be
     * deleted by this processor, even if a pre-processing-command to
     * delete the group is encountered.
     * 
     * @param group
     */
public List<String> getTriggerGroupsToNeverDelete() {
    return Collections.unmodifiableList(triggerGroupsToDelete);
}
