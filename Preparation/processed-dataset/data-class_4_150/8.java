protected Scheduler instantiate(QuartzSchedulerResources rsrcs, QuartzScheduler qs) {
    SchedulingContext schedCtxt = new SchedulingContext();
    schedCtxt.setInstanceId(rsrcs.getInstanceId());
    Scheduler scheduler = new StdScheduler(qs, schedCtxt);
    return scheduler;
}
