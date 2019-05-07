/**
     * Returns a <code>List</code> of jobs loaded from the xml file.
     * <p/>
     * 
     * @return a <code>List</code> of jobs.
     */
protected List<JobDetail> getLoadedJobs() {
    return Collections.unmodifiableList(loadedJobs);
}
