private String getSchedulerName() {
    return cfg.getStringProperty(PROP_SCHED_INSTANCE_NAME, "QuartzScheduler");
}
