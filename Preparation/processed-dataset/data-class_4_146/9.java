/**
     * Get the (unmodifiable) list of job groups that will never be
     * deleted by this processor, even if a pre-processing-command to
     * delete the group is encountered.
     * 
     * @param group
     */
public List<String> getJobGroupsToNeverDelete() {
    return Collections.unmodifiableList(jobGroupsToDelete);
}
