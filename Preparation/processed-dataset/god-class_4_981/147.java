protected void clusterRecover(Connection conn, List failedInstances) throws JobPersistenceException {
    if (failedInstances.size() > 0) {
        long recoverIds = System.currentTimeMillis();
        logWarnIfNonZero(failedInstances.size(), "ClusterManager: detected " + failedInstances.size() + " failed or restarted instances.");
        try {
            Iterator itr = failedInstances.iterator();
            while (itr.hasNext()) {
                SchedulerStateRecord rec = (SchedulerStateRecord) itr.next();
                getLog().info("ClusterManager: Scanning for instance \"" + rec.getSchedulerInstanceId() + "\"'s failed in-progress jobs.");
                List firedTriggerRecs = getDelegate().selectInstancesFiredTriggerRecords(conn, rec.getSchedulerInstanceId());
                int acquiredCount = 0;
                int recoveredCount = 0;
                int otherCount = 0;
                Set triggerKeys = new HashSet();
                Iterator ftItr = firedTriggerRecs.iterator();
                while (ftItr.hasNext()) {
                    FiredTriggerRecord ftRec = (FiredTriggerRecord) ftItr.next();
                    Key tKey = ftRec.getTriggerKey();
                    Key jKey = ftRec.getJobKey();
                    triggerKeys.add(tKey);
                    // release blocked triggers.. 
                    if (ftRec.getFireInstanceState().equals(STATE_BLOCKED)) {
                        getDelegate().updateTriggerStatesForJobFromOtherState(conn, jKey.getName(), jKey.getGroup(), STATE_WAITING, STATE_BLOCKED);
                    } else if (ftRec.getFireInstanceState().equals(STATE_PAUSED_BLOCKED)) {
                        getDelegate().updateTriggerStatesForJobFromOtherState(conn, jKey.getName(), jKey.getGroup(), STATE_PAUSED, STATE_PAUSED_BLOCKED);
                    }
                    // release acquired triggers.. 
                    if (ftRec.getFireInstanceState().equals(STATE_ACQUIRED)) {
                        getDelegate().updateTriggerStateFromOtherState(conn, tKey.getName(), tKey.getGroup(), STATE_WAITING, STATE_ACQUIRED);
                        acquiredCount++;
                    } else if (ftRec.isJobRequestsRecovery()) {
                        // handle jobs marked for recovery that were not fully 
                        // executed.. 
                        if (jobExists(conn, jKey.getName(), jKey.getGroup())) {
                            SimpleTrigger rcvryTrig = new SimpleTrigger("recover_" + rec.getSchedulerInstanceId() + "_" + String.valueOf(recoverIds++), Scheduler.DEFAULT_RECOVERY_GROUP, new Date(ftRec.getFireTimestamp()));
                            rcvryTrig.setVolatility(ftRec.isTriggerIsVolatile());
                            rcvryTrig.setJobName(jKey.getName());
                            rcvryTrig.setJobGroup(jKey.getGroup());
                            rcvryTrig.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                            rcvryTrig.setPriority(ftRec.getPriority());
                            JobDataMap jd = getDelegate().selectTriggerJobDataMap(conn, tKey.getName(), tKey.getGroup());
                            jd.put(Scheduler.FAILED_JOB_ORIGINAL_TRIGGER_NAME, tKey.getName());
                            jd.put(Scheduler.FAILED_JOB_ORIGINAL_TRIGGER_GROUP, tKey.getGroup());
                            jd.put(Scheduler.FAILED_JOB_ORIGINAL_TRIGGER_FIRETIME_IN_MILLISECONDS, String.valueOf(ftRec.getFireTimestamp()));
                            rcvryTrig.setJobDataMap(jd);
                            rcvryTrig.computeFirstFireTime(null);
                            storeTrigger(conn, null, rcvryTrig, null, false, STATE_WAITING, false, true);
                            recoveredCount++;
                        } else {
                            getLog().warn("ClusterManager: failed job '" + jKey + "' no longer exists, cannot schedule recovery.");
                            otherCount++;
                        }
                    } else {
                        otherCount++;
                    }
                    // free up stateful job's triggers 
                    if (ftRec.isJobIsStateful()) {
                        getDelegate().updateTriggerStatesForJobFromOtherState(conn, jKey.getName(), jKey.getGroup(), STATE_WAITING, STATE_BLOCKED);
                        getDelegate().updateTriggerStatesForJobFromOtherState(conn, jKey.getName(), jKey.getGroup(), STATE_PAUSED, STATE_PAUSED_BLOCKED);
                    }
                }
                getDelegate().deleteFiredTriggers(conn, rec.getSchedulerInstanceId());
                // Check if any of the fired triggers we just deleted were the last fired trigger 
                // records of a COMPLETE trigger. 
                int completeCount = 0;
                for (Iterator triggerKeyIter = triggerKeys.iterator(); triggerKeyIter.hasNext(); ) {
                    Key triggerKey = (Key) triggerKeyIter.next();
                    if (getDelegate().selectTriggerState(conn, triggerKey.getName(), triggerKey.getGroup()).equals(STATE_COMPLETE)) {
                        List firedTriggers = getDelegate().selectFiredTriggerRecords(conn, triggerKey.getName(), triggerKey.getGroup());
                        if (firedTriggers.isEmpty()) {
                            SchedulingContext schedulingContext = new SchedulingContext();
                            schedulingContext.setInstanceId(instanceId);
                            if (removeTrigger(conn, schedulingContext, triggerKey.getName(), triggerKey.getGroup())) {
                                completeCount++;
                            }
                        }
                    }
                }
                logWarnIfNonZero(acquiredCount, "ClusterManager: ......Freed " + acquiredCount + " acquired trigger(s).");
                logWarnIfNonZero(completeCount, "ClusterManager: ......Deleted " + completeCount + " complete triggers(s).");
                logWarnIfNonZero(recoveredCount, "ClusterManager: ......Scheduled " + recoveredCount + " recoverable job(s) for recovery.");
                logWarnIfNonZero(otherCount, "ClusterManager: ......Cleaned-up " + otherCount + " other failed job(s).");
                if (rec.getSchedulerInstanceId().equals(getInstanceId()) == false) {
                    getDelegate().deleteSchedulerState(conn, rec.getSchedulerInstanceId());
                }
            }
        } catch (Exception e) {
            throw new JobPersistenceException("Failure recovering jobs: " + e.getMessage(), e);
        }
    }
}
