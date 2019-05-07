/**
     * Create target instances for the target.
     * @param aSchedule The schedule to use.
     */
public void createTargetInstances(AbstractTarget aTarget, Schedule aSchedule, boolean checkAgency) {
    int objectType = aTarget.getObjectType();
    if (objectType == AbstractTarget.TYPE_GROUP) {
        //Lets make sure it is loaded before we start casting!  
        if (!(aTarget instanceof TargetGroup)) {
            aTarget = targetDao.loadGroup(aTarget.getOid());
        }
    }
    if (objectType == AbstractTarget.TYPE_TARGET || objectType == AbstractTarget.TYPE_GROUP && ((TargetGroup) aTarget).getSipType() == TargetGroup.ONE_SIP) {
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
            //targetDao.saveAll(targetInstances);  
            System.out.println("  Created " + targetInstances.size());
        }
    } else if (objectType == AbstractTarget.TYPE_GROUP && ((TargetGroup) aTarget).getSipType() == TargetGroup.MANY_SIP) {
        TargetGroup aTargetGroup = (TargetGroup) aTarget;
        for (GroupMember member : aTargetGroup.getChildren()) {
            //If the child is a Sub-Group, we don't want to propagate the schedule to its members  
            AbstractTarget child = member.getChild();
            if (child.getObjectType() == AbstractTarget.TYPE_GROUP) {
                TargetGroup childGroup = targetDao.loadGroup(child.getOid(), false);
                if (subGroupTypeName.equals(childGroup.getType())) {
                    continue;
                }
            }
            // Only want to add them if they're unique.  
            createTargetInstances(child, aSchedule, checkAgency);
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
