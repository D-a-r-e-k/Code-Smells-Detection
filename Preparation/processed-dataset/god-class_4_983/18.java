/**
     * <p>
     * Returns a handle to all known Schedulers (made by any
     * StdSchedulerFactory instance.).
     * </p>
     */
public Collection getAllSchedulers() throws SchedulerException {
    return SchedulerRepository.getInstance().lookupAll();
}
