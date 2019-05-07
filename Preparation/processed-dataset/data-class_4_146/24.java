/**
     * Process the xml file in the given location, and schedule all of the
     * jobs defined within it.
     * 
     * @param fileName
     *          meta data file name.
     */
public void processFileAndScheduleJobs(String fileName, Scheduler sched) throws Exception {
    processFileAndScheduleJobs(fileName, getSystemIdForFileName(fileName), sched);
}
