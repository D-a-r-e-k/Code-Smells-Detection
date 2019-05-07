protected void executePreProcessCommands(Scheduler scheduler) throws SchedulerException {
    for (String group : jobGroupsToDelete) {
        if (group.equals("*")) {
            log.info("Deleting all jobs in ALL groups.");
            for (String groupName : scheduler.getJobGroupNames()) {
                if (!jobGroupsToNeverDelete.contains(groupName)) {
                    for (String jobName : scheduler.getJobNames(groupName)) {
                        scheduler.deleteJob(jobName, groupName);
                    }
                }
            }
        } else {
            if (!jobGroupsToNeverDelete.contains(group)) {
                log.info("Deleting all jobs in group: {}", group);
                for (String jobName : scheduler.getJobNames(group)) {
                    scheduler.deleteJob(jobName, group);
                }
            }
        }
    }
    for (String group : triggerGroupsToDelete) {
        if (group.equals("*")) {
            log.info("Deleting all triggers in ALL groups.");
            for (String groupName : scheduler.getTriggerGroupNames()) {
                if (!triggerGroupsToNeverDelete.contains(groupName)) {
                    for (String triggerName : scheduler.getTriggerNames(groupName)) {
                        scheduler.unscheduleJob(triggerName, groupName);
                    }
                }
            }
        } else {
            if (!triggerGroupsToNeverDelete.contains(group)) {
                log.info("Deleting all triggers in group: {}", group);
                for (String triggerName : scheduler.getTriggerNames(group)) {
                    scheduler.unscheduleJob(triggerName, group);
                }
            }
        }
    }
    for (Key key : jobsToDelete) {
        if (!jobGroupsToNeverDelete.contains(key.getGroup())) {
            log.info("Deleting job: {}", key);
            scheduler.deleteJob(key.getName(), key.getGroup());
        }
    }
    for (Key key : triggersToDelete) {
        if (!triggerGroupsToNeverDelete.contains(key.getGroup())) {
            log.info("Deleting trigger: {}", key);
            scheduler.unscheduleJob(key.getName(), key.getGroup());
        }
    }
}
