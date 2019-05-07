/**
     * Process the xml file in the given location, and schedule all of the
     * jobs defined within it.
     * 
     * @param fileName
     *          meta data file name.
     */
public void processFileAndScheduleJobs(String fileName, String systemId, Scheduler sched) throws Exception {
    processFile(fileName, systemId);
    executePreProcessCommands(sched);
    scheduleJobs(sched);
}
