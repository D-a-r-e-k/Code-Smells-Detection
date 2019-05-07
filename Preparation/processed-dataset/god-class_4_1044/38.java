/**
	 * Look up all schedules that need to be processed.
	 */
public void processSchedulesJob() {
    List<Schedule> schedules = targetDao.getSchedulesToRun();
    // STK - use below line instead of above line in test environment due to performance issues.   
    // List<Schedule> schedules = new LinkedList<Schedule>();  
    System.out.println("Batch processing " + schedules.size() + " schedules.");
    for (Schedule s : schedules) {
        if (s.getTarget() == null) {
            System.out.println(" Schedule has null target so skipping processing: " + s.getOid());
            log.debug(" Schedule has null target so skipping processing: " + s.getOid());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            s.setLastProcessedDate(cal.getTime());
            targetDao.save(s);
            System.out.println(" Saved schedule: " + s.getOid() + " - set last processed date to: " + cal.getTime());
        } else {
            log.debug(" Processing schedule: " + s.getOid());
            processBatchSchedule(s);
        }
    }
}
