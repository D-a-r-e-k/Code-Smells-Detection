/**
	 * Run the checks to see if the target instance can be harvested or if it must be queued.
	 * If harvest is possible and there is a harvester available then allocate it.
	 * @param aTargetInstance the target instance to harvest
	 */
public void harvestOrQueue(QueuedTargetInstanceDTO aTargetInstance) {
    TargetInstance ti = null;
    boolean approved = false;
    if (TargetInstance.STATE_SCHEDULED.equals(aTargetInstance.getState())) {
        ti = targetInstanceDao.load(aTargetInstance.getOid());
        ti = targetInstanceDao.populate(ti);
        approved = isTargetApproved(ti);
    } else {
        approved = true;
    }
    if (approved) {
        boolean processed = false;
        while (!processed) {
            // Check to see what harvester resource is available  
            HarvestAgentStatusDTO agent = getHarvester(aTargetInstance.getAgencyName());
            if (!queuePaused && isMiniumBandwidthAvailable(aTargetInstance) && agent != null) {
                synchronized (agent) {
                    // allocate the target instance to the agent  
                    if (ti == null) {
                        ti = targetInstanceDao.load(aTargetInstance.getOid());
                        ti = targetInstanceDao.populate(ti);
                    }
                    try {
                        if (!TargetInstance.STATE_QUEUED.equals(ti.getState())) {
                            prepareHarvest(ti);
                        }
                        _harvest(ti, agent);
                        agent.setInTransition(true);
                        processed = true;
                    } catch (Throwable e) {
                        if (log.isWarnEnabled()) {
                            log.warn("Failed to allocate harvest to agent " + agent.getName() + ": " + e.getMessage(), e);
                        }
                        harvestAgents.remove(agent.getName());
                    }
                }
            } else {
                processed = true;
                // if not already queued set the target instance to the queued state.  
                if (!aTargetInstance.getState().equals(TargetInstance.STATE_QUEUED)) {
                    if (ti == null) {
                        ti = targetInstanceDao.load(aTargetInstance.getOid());
                        ti = targetInstanceDao.populate(ti);
                    }
                    // Prepare the harvest for the queue.  
                    prepareHarvest(ti);
                    ti.setState(TargetInstance.STATE_QUEUED);
                    targetInstanceDao.save(ti);
                    inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_QUEUED, ti);
                }
            }
        }
    }
}
