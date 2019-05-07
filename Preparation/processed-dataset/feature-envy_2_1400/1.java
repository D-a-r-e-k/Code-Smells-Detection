/**
     * Schedules the given sets of jobs and triggers.
     * 
     * @param sched
     *          job scheduler.
     * @exception SchedulerException
     *              if the Job or Trigger cannot be added to the Scheduler, or
     *              there is an internal Scheduler error.
     */
protected void scheduleJobs(Scheduler sched) throws SchedulerException {
    List<JobDetail> jobs = new LinkedList(getLoadedJobs());
    List<Trigger> triggers = new LinkedList(getLoadedTriggers());
    log.info("Adding " + jobs.size() + " jobs, " + triggers.size() + " triggers.");
    Map<String, List<Trigger>> triggersByFQJobName = buildTriggersByFQJobNameMap(triggers);
    // add each job, and it's associated triggers 
    Iterator<JobDetail> itr = jobs.iterator();
    while (itr.hasNext()) {
        JobDetail detail = itr.next();
        itr.remove();
        // remove jobs as we handle them... 
        JobDetail dupeJ = sched.getJobDetail(detail.getName(), detail.getGroup());
        if ((dupeJ != null)) {
            if (!isOverWriteExistingData() && isIgnoreDuplicates()) {
                log.info("Not overwriting existing job: " + dupeJ.getFullName());
                continue;
            }
            if (!isOverWriteExistingData() && !isIgnoreDuplicates()) {
                throw new ObjectAlreadyExistsException(detail);
            }
        }
        if (dupeJ != null) {
            log.info("Replacing job: " + detail.getFullName());
        } else {
            log.info("Adding job: " + detail.getFullName());
        }
        List<Trigger> triggersOfJob = triggersByFQJobName.get(detail.getFullName());
        if (!detail.isDurable() && (triggersOfJob == null || triggersOfJob.size() == 0)) {
            if (dupeJ == null) {
                throw new SchedulerException("A new job defined without any triggers must be durable: " + detail.getFullName());
            }
            if ((dupeJ.isDurable() && (sched.getTriggersOfJob(detail.getName(), detail.getGroup()).length == 0))) {
                throw new SchedulerException("Can't change existing durable job without triggers to non-durable: " + detail.getFullName());
            }
        }
        if (dupeJ != null || detail.isDurable()) {
            sched.addJob(detail, true);
        } else {
            boolean addJobWithFirstSchedule = true;
            // Add triggers related to the job... 
            Iterator<Trigger> titr = triggersOfJob.iterator();
            while (titr.hasNext()) {
                Trigger trigger = titr.next();
                triggers.remove(trigger);
                // remove triggers as we handle them... 
                if (trigger.getStartTime() == null) {
                    trigger.setStartTime(new Date());
                }
                boolean addedTrigger = false;
                while (addedTrigger == false) {
                    Trigger dupeT = sched.getTrigger(trigger.getName(), trigger.getGroup());
                    if (dupeT != null) {
                        if (isOverWriteExistingData()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Rescheduling job: " + trigger.getFullJobName() + " with updated trigger: " + trigger.getFullName());
                            }
                        } else if (isIgnoreDuplicates()) {
                            log.info("Not overwriting existing trigger: " + dupeT.getFullName());
                            continue;
                        } else {
                            throw new ObjectAlreadyExistsException(trigger);
                        }
                        if (!dupeT.getJobGroup().equals(trigger.getJobGroup()) || !dupeT.getJobName().equals(trigger.getJobName())) {
                            log.warn("Possibly duplicately named ({}) triggers in jobs xml file! ", trigger.getFullName());
                        }
                        sched.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Scheduling job: " + trigger.getFullJobName() + " with trigger: " + trigger.getFullName());
                        }
                        try {
                            if (addJobWithFirstSchedule) {
                                sched.scheduleJob(detail, trigger);
                                // add the job if it's not in yet... 
                                addJobWithFirstSchedule = false;
                            } else {
                                sched.scheduleJob(trigger);
                            }
                        } catch (ObjectAlreadyExistsException e) {
                            if (log.isDebugEnabled()) {
                                log.debug("Adding trigger: " + trigger.getFullName() + " for job: " + detail.getFullName() + " failed because the trigger already existed.  " + "This is likely due to a race condition between multiple instances " + "in the cluster.  Will try to reschedule instead.");
                            }
                            continue;
                        }
                    }
                    addedTrigger = true;
                }
            }
        }
    }
    // add triggers that weren't associated with a new job... (those we already handled were removed above) 
    for (Trigger trigger : triggers) {
        if (trigger.getStartTime() == null) {
            trigger.setStartTime(new Date());
        }
        boolean addedTrigger = false;
        while (addedTrigger == false) {
            Trigger dupeT = sched.getTrigger(trigger.getName(), trigger.getGroup());
            if (dupeT != null) {
                if (isOverWriteExistingData()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Rescheduling job: " + trigger.getFullJobName() + " with updated trigger: " + trigger.getFullName());
                    }
                } else if (isIgnoreDuplicates()) {
                    log.info("Not overwriting existing trigger: " + dupeT.getFullName());
                    continue;
                } else {
                    throw new ObjectAlreadyExistsException(trigger);
                }
                if (!dupeT.getJobGroup().equals(trigger.getJobGroup()) || !dupeT.getJobName().equals(trigger.getJobName())) {
                    log.warn("Possibly duplicately named ({}) triggers in jobs xml file! ", trigger.getFullName());
                }
                sched.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Scheduling job: " + trigger.getFullJobName() + " with trigger: " + trigger.getFullName());
                }
                try {
                    sched.scheduleJob(trigger);
                } catch (ObjectAlreadyExistsException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Adding trigger: " + trigger.getFullName() + " for job: " + trigger.getFullJobName() + " failed because the trigger already existed.  " + "This is likely due to a race condition between multiple instances " + "in the cluster.  Will try to reschedule instead.");
                    }
                    continue;
                }
            }
            addedTrigger = true;
        }
    }
}
