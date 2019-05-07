/**
     * Create target instances for the target.
     * @param aSchedule The schedule to use.
     */
public void createBatchTargetInstances(AbstractTarget aTarget, Schedule aSchedule, TargetGroup aGroup, boolean checkAgency) {
    int objectType = aTarget.getObjectType();
    if (objectType == AbstractTarget.TYPE_TARGET || (objectType == AbstractTarget.TYPE_GROUP && aGroup.getSipType() == TargetGroup.ONE_SIP)) {
        if (checkAgency && !aTarget.getOwner().getAgency().equals(aSchedule.getOwningUser().getAgency())) {
            // We should not schedule this one because it belongs to a different  
            // agency to the schedule.  
            return;
        } else if (!aTarget.isSchedulable()) {
            // We cannot schedule an unschedulable target.  
            return;
        } else {
            Set<TargetInstance> targetInstances = new HashSet<TargetInstance>();
            // Get the schedule ahead time from our environment.  
            int daysToSchedule = EnvironmentFactory.getEnv().getDaysToSchedule();
            // Determine when to end the schedule. This is the earliest of the   
            // end date, or the current date + the number of days ahead to schedule.  
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, daysToSchedule);
            Date scheduleTill = aSchedule.getEndDate() == null ? cal.getTime() : DateUtils.earliestDate(aSchedule.getEndDate(), cal.getTime());
            // Determine when to schedule from. This is the latest of:  
            //  1. Today  
            //  2. The start date.  
            //  3. The latest current target instance.  
            Date startFrom = targetDao.getLatestScheduledDate(aTarget, aSchedule);
            if (startFrom == null) {
                startFrom = new Date();
            }
            startFrom = DateUtils.latestDate(startFrom, aSchedule.getStartDate());
            List<Annotation> targetAnnotations = getAnnotations(aTarget);
            boolean firstForTarget = false;
            for (startFrom = aSchedule.getNextExecutionDate(startFrom); startFrom != null && startFrom.before(scheduleTill); startFrom = aSchedule.getNextExecutionDate(startFrom)) {
                TargetInstance ti = new TargetInstance();
                ti.setTarget(aTarget);
                ti.setSchedule(aSchedule);
                ti.setScheduledTime(startFrom);
                ti.setOwner(aSchedule.getOwningUser());
                // if this is the first TargetInstance ever to be created for  
                // a particular target then set the firstFromTarget flag on the  
                // TI.  
                if (!firstForTarget && instanceManager.countTargetInstancesByTarget(aTarget.getOid()) == 0) {
                    ti.setFirstFromTarget(true);
                    firstForTarget = true;
                }
                if (objectType == AbstractTarget.TYPE_TARGET) {
                    // copy 'alert' type annotations from a target type to each TI  
                    for (Iterator<Annotation> i = targetAnnotations.iterator(); i.hasNext(); ) {
                        Annotation toCopy = i.next();
                        if (toCopy.isAlertable()) {
                            Annotation annotation = new Annotation();
                            annotation.setDate(toCopy.getDate());
                            annotation.setNote(toCopy.getNote());
                            annotation.setAlertable(true);
                            annotation.setUser(toCopy.getUser());
                            annotation.setObjectType(TargetInstance.class.getName());
                            ti.addAnnotation(annotation);
                        }
                    }
                    // if the useAQA flag is set on the Target then set  
                    // it on the Target Instance too.  
                    ti.setUseAQA(isTargetUsingAQA(aTarget.getOid()));
                }
                targetInstances.add(ti);
            }
            for (TargetInstance toSave : targetInstances) {
                targetInstanceDao.save(toSave);
                // Update the OIDs for the target.  
                for (Annotation anno : toSave.getAnnotations()) {
                    anno.setObjectOid(toSave.getOid());
                }
                // Save the annotations  
                annotationDAO.saveAnnotations(toSave.getAnnotations());
                log.debug(" Saved TI: " + toSave.getOid());
            }
            log.debug(" Created " + targetInstances.size());
        }
    } else if (objectType == AbstractTarget.TYPE_GROUP && aGroup.getSipType() == TargetGroup.MANY_SIP) {
        for (GroupMember member : aGroup.getChildren()) {
            //If the child is a Sub-Group, we don't want to propagate the schedule to its members  
            AbstractTarget child = targetDao.load(member.getChild().getOid());
            if (child.getObjectType() == AbstractTarget.TYPE_GROUP) {
                continue;
            }
            // Only want to add them if they're unique.  
            createBatchTargetInstances(child, aSchedule, null, checkAgency);
        }
        return;
    } else {
        String type = null;
        if (aTarget instanceof TargetGroup) {
            type = "" + ((TargetGroup) aTarget).getSipType();
        }
        throw new WCTRuntimeException("Unknown Target Type: " + aTarget.getClass() + " (" + type + ")");
    }
}
