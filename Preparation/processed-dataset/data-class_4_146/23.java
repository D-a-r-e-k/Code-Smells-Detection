/**
     * Process the xml file in the default location, and schedule all of the
     * jobs defined within it.
     *  
     */
public void processFileAndScheduleJobs(Scheduler sched, boolean overWriteExistingJobs) throws SchedulerException, Exception {
    processFileAndScheduleJobs(QUARTZ_XML_DEFAULT_FILE_NAME, sched);
}
