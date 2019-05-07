/**
     * <p>
     * Insert or update a trigger.
     * </p>
     */
protected void storeTrigger(Connection conn, SchedulingContext ctxt, Trigger newTrigger, JobDetail job, boolean replaceExisting, String state, boolean forceState, boolean recovering) throws ObjectAlreadyExistsException, JobPersistenceException {
    if (newTrigger.isVolatile() && isClustered()) {
        getLog().info("note: volatile triggers are effectively non-volatile in a clustered environment.");
    }
    boolean existingTrigger = triggerExists(conn, newTrigger.getName(), newTrigger.getGroup());
    if ((existingTrigger) && (!replaceExisting)) {
        throw new ObjectAlreadyExistsException(newTrigger);
    }
    try {
        boolean shouldBepaused = false;
        if (!forceState) {
            shouldBepaused = getDelegate().isTriggerGroupPaused(conn, newTrigger.getGroup());
            if (!shouldBepaused) {
                shouldBepaused = getDelegate().isTriggerGroupPaused(conn, ALL_GROUPS_PAUSED);
                if (shouldBepaused) {
                    getDelegate().insertPausedTriggerGroup(conn, newTrigger.getGroup());
                }
            }
            if (shouldBepaused && (state.equals(STATE_WAITING) || state.equals(STATE_ACQUIRED))) {
                state = STATE_PAUSED;
            }
        }
        if (job == null) {
            job = getDelegate().selectJobDetail(conn, newTrigger.getJobName(), newTrigger.getJobGroup(), getClassLoadHelper());
        }
        if (job == null) {
            throw new JobPersistenceException("The job (" + newTrigger.getFullJobName() + ") referenced by the trigger does not exist.");
        }
        if (job.isVolatile() && !newTrigger.isVolatile()) {
            throw new JobPersistenceException("It does not make sense to " + "associate a non-volatile Trigger with a volatile Job!");
        }
        if (job.isStateful() && !recovering) {
            state = checkBlockedState(conn, ctxt, job.getName(), job.getGroup(), state);
        }
        if (existingTrigger) {
            if (newTrigger instanceof SimpleTrigger && ((SimpleTrigger) newTrigger).hasAdditionalProperties() == false) {
                getDelegate().updateSimpleTrigger(conn, (SimpleTrigger) newTrigger);
            } else if (newTrigger instanceof CronTrigger && ((CronTrigger) newTrigger).hasAdditionalProperties() == false) {
                getDelegate().updateCronTrigger(conn, (CronTrigger) newTrigger);
            } else {
                getDelegate().updateBlobTrigger(conn, newTrigger);
            }
            getDelegate().updateTrigger(conn, newTrigger, state, job);
        } else {
            getDelegate().insertTrigger(conn, newTrigger, state, job);
            if (newTrigger instanceof SimpleTrigger && ((SimpleTrigger) newTrigger).hasAdditionalProperties() == false) {
                getDelegate().insertSimpleTrigger(conn, (SimpleTrigger) newTrigger);
            } else if (newTrigger instanceof CronTrigger && ((CronTrigger) newTrigger).hasAdditionalProperties() == false) {
                getDelegate().insertCronTrigger(conn, (CronTrigger) newTrigger);
            } else {
                getDelegate().insertBlobTrigger(conn, newTrigger);
            }
        }
    } catch (Exception e) {
        throw new JobPersistenceException("Couldn't store trigger '" + newTrigger.getName() + "' for '" + newTrigger.getJobName() + "' job:" + e.getMessage(), e);
    }
}
