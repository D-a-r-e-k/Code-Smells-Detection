protected void triggeredJobComplete(Connection conn, SchedulingContext ctxt, Trigger trigger, JobDetail jobDetail, int triggerInstCode) throws JobPersistenceException {
    try {
        if (triggerInstCode == Trigger.INSTRUCTION_DELETE_TRIGGER) {
            if (trigger.getNextFireTime() == null) {
                // double check for possible reschedule within job  
                // execution, which would cancel the need to delete... 
                TriggerStatus stat = getDelegate().selectTriggerStatus(conn, trigger.getName(), trigger.getGroup());
                if (stat != null && stat.getNextFireTime() == null) {
                    removeTrigger(conn, ctxt, trigger.getName(), trigger.getGroup());
                }
            } else {
                removeTrigger(conn, ctxt, trigger.getName(), trigger.getGroup());
                signalSchedulingChangeOnTxCompletion(0L);
            }
        } else if (triggerInstCode == Trigger.INSTRUCTION_SET_TRIGGER_COMPLETE) {
            getDelegate().updateTriggerState(conn, trigger.getName(), trigger.getGroup(), STATE_COMPLETE);
            signalSchedulingChangeOnTxCompletion(0L);
        } else if (triggerInstCode == Trigger.INSTRUCTION_SET_TRIGGER_ERROR) {
            getLog().info("Trigger " + trigger.getFullName() + " set to ERROR state.");
            getDelegate().updateTriggerState(conn, trigger.getName(), trigger.getGroup(), STATE_ERROR);
            signalSchedulingChangeOnTxCompletion(0L);
        } else if (triggerInstCode == Trigger.INSTRUCTION_SET_ALL_JOB_TRIGGERS_COMPLETE) {
            getDelegate().updateTriggerStatesForJob(conn, trigger.getJobName(), trigger.getJobGroup(), STATE_COMPLETE);
            signalSchedulingChangeOnTxCompletion(0L);
        } else if (triggerInstCode == Trigger.INSTRUCTION_SET_ALL_JOB_TRIGGERS_ERROR) {
            getLog().info("All triggers of Job " + trigger.getFullJobName() + " set to ERROR state.");
            getDelegate().updateTriggerStatesForJob(conn, trigger.getJobName(), trigger.getJobGroup(), STATE_ERROR);
            signalSchedulingChangeOnTxCompletion(0L);
        }
        if (jobDetail.isStateful()) {
            getDelegate().updateTriggerStatesForJobFromOtherState(conn, jobDetail.getName(), jobDetail.getGroup(), STATE_WAITING, STATE_BLOCKED);
            getDelegate().updateTriggerStatesForJobFromOtherState(conn, jobDetail.getName(), jobDetail.getGroup(), STATE_PAUSED, STATE_PAUSED_BLOCKED);
            signalSchedulingChangeOnTxCompletion(0L);
            try {
                if (jobDetail.getJobDataMap().isDirty()) {
                    getDelegate().updateJobData(conn, jobDetail);
                }
            } catch (IOException e) {
                throw new JobPersistenceException("Couldn't serialize job data: " + e.getMessage(), e);
            } catch (SQLException e) {
                throw new JobPersistenceException("Couldn't update job data: " + e.getMessage(), e);
            }
        }
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't update trigger state(s): " + e.getMessage(), e);
    }
    try {
        getDelegate().deleteFiredTrigger(conn, trigger.getFireInstanceId());
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't delete fired trigger: " + e.getMessage(), e);
    }
}
