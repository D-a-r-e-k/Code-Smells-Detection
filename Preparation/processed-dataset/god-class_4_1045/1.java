/**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#heartbeat(org.webcurator.core.harvester.agent.HarvestAgentStatus)
     */
public void heartbeat(HarvestAgentStatusDTO aStatus) {
    if (log.isInfoEnabled()) {
        if (harvestAgents.containsKey(aStatus.getName())) {
            if (log.isDebugEnabled()) {
                log.debug("Updating status for " + aStatus.getName());
            }
        } else {
            log.info("Registering harvest agent " + aStatus.getName());
        }
    }
    aStatus.setLastUpdated(new Date());
    harvestAgents.put(aStatus.getName(), aStatus);
    String key = "";
    long toid = 0;
    TargetInstance ti = null;
    HarvesterStatusDTO sd = null;
    HashMap hss = null;
    HarvesterStatus status = null;
    Iterator it = aStatus.getHarvesterStatus().keySet().iterator();
    while (it.hasNext()) {
        key = (String) it.next();
        toid = Long.parseLong(key.substring(key.lastIndexOf("-") + 1));
        ti = targetInstanceDao.load(toid);
        hss = aStatus.getHarvesterStatus();
        sd = (HarvesterStatusDTO) hss.get(key);
        //Update the harvesterStatus with current versions  
        Environment env = EnvironmentFactory.getEnv();
        sd.setApplicationVersion(env.getApplicationVersion());
        sd.setHeritrixVersion(env.getHeritrixVersion());
        if (ti.getStatus() == null) {
            status = new HarvesterStatus(sd);
            ti.setStatus(status);
            status.setOid(ti.getOid());
        } else {
            status = ti.getStatus();
            status.update(sd);
        }
        if (status.getStatus().startsWith("Paused")) {
            if (ti.getState().equals(TargetInstance.STATE_RUNNING)) {
                ti.setState(TargetInstance.STATE_PAUSED);
            }
        }
        //We have seen cases where a running Harvest is showing as Queued   
        //in the UI. Once in this state, the user has no control over the   
        //harvest and cannot use it. This work around means that any   
        //TIs in the wrong state will be corrected on the next heartbeat  
        if (status.getStatus().startsWith("Running")) {
            if (ti.getState().equals(TargetInstance.STATE_PAUSED) || ti.getState().equals(TargetInstance.STATE_QUEUED)) {
                if (ti.getState().equals(TargetInstance.STATE_QUEUED)) {
                    log.info("HarvestCoordinator: Target Instance state changed from Queued to Running for target instance " + ti.getOid().toString());
                }
                if (ti.getActualStartTime() == null) {
                    //This was not set up correctly when harvest was initiated  
                    Date now = new Date();
                    Date startTime = new Date(now.getTime() - status.getElapsedTime());
                    ti.setActualStartTime(startTime);
                    ti.setHarvestServer(aStatus.getName());
                    log.info("HarvestCoordinator: Target Instance start time set for target instance " + ti.getOid().toString());
                }
                ti.setState(TargetInstance.STATE_RUNNING);
            }
        }
        if (status.getStatus().startsWith("Finished")) {
            if (ti.getState().equals(TargetInstance.STATE_RUNNING)) {
                ti.setState(TargetInstance.STATE_STOPPING);
            }
        }
        // This is a required because when a "Could not launch job - Fatal InitializationException" job occurs  
        // We do not get a notification that causes the job to stop nicely  
        if (status.getStatus().startsWith("Could not launch job - Fatal InitializationException")) {
            if (ti.getState().equals(TargetInstance.STATE_RUNNING)) {
                ti.setState(TargetInstance.STATE_ABORTED);
                HarvestAgentStatusDTO hs = getHarvestAgent(ti.getJobName());
                if (hs == null) {
                    if (log.isWarnEnabled()) {
                        log.warn("Forced Abort Failed. Failed to find the Harvest Agent for the Job " + ti.getJobName() + ".");
                    }
                } else {
                    HarvestAgent agent = harvestAgentFactory.getHarvestAgent(hs.getHost(), hs.getPort());
                    agent.abort(ti.getJobName());
                }
            }
        }
        targetInstanceManager.save(ti);
    }
}
