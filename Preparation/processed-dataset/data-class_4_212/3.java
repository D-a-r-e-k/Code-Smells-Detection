/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#save(org.webcurator.domain.model.core.Target)
	 */
public void save(Target aTarget, List<GroupMemberDTO> parents) {
    boolean newSchedulesAddedByNonOwner = false;
    boolean wasHarvestNowSelected = aTarget.isHarvestNow();
    // If dirty and the current state is approved, we need to change the  
    // state back to nominated.  
    if (aTarget.isDirty() && aTarget.getOriginalState() == Target.STATE_APPROVED && !authMgr.hasPrivilege(aTarget, Privilege.APPROVE_TARGET)) {
        log.debug("Target state changed to nominated due to changes");
        aTarget.changeState(Target.STATE_NOMINATED);
    }
    // Track a change into the approved state.  
    if ((aTarget.getOriginalState() == Target.STATE_PENDING || aTarget.getOriginalState() == Target.STATE_REINSTATED || aTarget.getOriginalState() == Target.STATE_NOMINATED) && aTarget.getState() == Target.STATE_APPROVED) {
        aTarget.setSelectionDate(new Date());
    }
    int numActiveTIsPrevious = targetInstanceDao.countActiveTIsForTarget(aTarget.getOid());
    // Deal with removed schedules  
    for (Schedule schedule : aTarget.getRemovedSchedules()) {
        targetInstanceDao.deleteScheduledInstances(schedule);
        schedule.setTargetInstances(new HashSet<TargetInstance>());
        targetInstanceDao.save(schedule);
    }
    // Deal with new schedules  
    List<Schedule> newSchedules = new LinkedList<Schedule>();
    for (Schedule schedule : aTarget.getSchedules()) {
        if (schedule.isNew()) {
            // Record the schedule for auditing after save.  
            newSchedules.add(schedule);
            // Send a notification if the schedule owner is not the same as  
            // the owner of the target.  
            if (!schedule.getOwningUser().equals(aTarget.getOwningUser())) {
                newSchedulesAddedByNonOwner = true;
            }
        }
    }
    // Load the original target so we can do auditing afterwards.  
    AbstractTargetDTO originalTarget = null;
    if (!aTarget.isNew()) {
        originalTarget = targetDao.loadAbstractTargetDTO(aTarget.getOid());
    }
    /* ---------------------------------------------------------------- */
    /* Deal with the scheduling state changes                           */
    /* ---------------------------------------------------------------- */
    // Get the original parents  
    Set<AbstractTargetDTO> origParents = Collections.EMPTY_SET;
    if (!aTarget.isNew()) {
        origParents = targetDao.getAncestorDTOs(aTarget.getOid());
    }
    // Save the target.  
    log.debug("Saving Target");
    int originalState = aTarget.getOriginalState();
    targetDao.save(aTarget, parents);
    /* ---------------------------------------------------------------- */
    /* Save the annotations												*/
    /* ---------------------------------------------------------------- */
    // Update the OIDs for the target.  
    for (Annotation anno : aTarget.getAnnotations()) {
        anno.setObjectOid(aTarget.getOid());
    }
    // Save the annotations  
    annotationDAO.saveAnnotations(aTarget.getAnnotations());
    annotationDAO.deleteAnnotations(aTarget.getDeletedAnnotations());
    aTarget = targetDao.reloadTarget(aTarget.getOid());
    log.debug("End of Save Target");
    /* ---------------------------------------------------------------- */
    /* Perform post-save auditing 										*/
    /* ---------------------------------------------------------------- */
    if (originalState != aTarget.getState()) {
        String newState = messageSource.getMessage("target.state_" + aTarget.getState(), null, Locale.getDefault());
        auditor.audit(Target.class.getName(), aTarget.getOid(), Auditor.ACTION_TARGET_STATE_CHANGE, "Target " + aTarget.getName() + " has changed into state '" + newState + "'");
    }
    for (Schedule schedule : newSchedules) {
        auditor.audit(Schedule.class.getName(), schedule.getOid(), Auditor.ACTION_NEW_SCHEDULE, "New Schedule Created on Target " + aTarget.getName());
    }
    if (originalTarget != null) {
        auditor.audit(Target.class.getName(), aTarget.getOid(), Auditor.ACTION_UPDATE_TARGET, "Target " + aTarget.getName() + " has been updated");
        if (!originalTarget.getOwnerOid().equals(aTarget.getOwningUser().getOid())) {
            auditor.audit(Target.class.getName(), aTarget.getOid(), Auditor.ACTION_TARGET_CHANGE_OWNER, "Target " + aTarget.getName() + " has been given to " + aTarget.getOwningUser().getNiceName());
        }
        if (!originalTarget.getProfileOid().equals(aTarget.getProfile().getOid())) {
            auditor.audit(Target.class.getName(), aTarget.getOid(), Auditor.ACTION_TARGET_CHANGE_PROFILE, "Target " + aTarget.getName() + " is now using profile " + aTarget.getProfile().getName());
        }
    }
    /* ---------------------------------------------------------------- */
    /* Check state changes and task/notification creation		 		*/
    /* ---------------------------------------------------------------- */
    if (originalState != aTarget.getState()) {
        if (aTarget.getState() == Target.STATE_NOMINATED) {
            intrayManager.generateTask(Privilege.APPROVE_TARGET, MessageType.TASK_APPROVE_TARGET, aTarget);
        }
        if (aTarget.getState() == Target.STATE_APPROVED || aTarget.getState() == Target.STATE_REJECTED) {
            intrayManager.deleteTask(aTarget.getOid(), aTarget.getResourceType(), MessageType.TASK_APPROVE_TARGET);
        }
        if (aTarget.getState() == Target.STATE_APPROVED && aTarget.isRunOnApproval()) {
            TargetInstance ti = new TargetInstance();
            ti.setTarget(aTarget);
            ti.setSchedule(null);
            ti.setScheduledTime(new Date());
            ti.setOwner(aTarget.getOwner());
            ti.setUseAQA(aTarget.isUseAQA());
            targetInstanceDao.save(ti);
        }
    } else {
        // state was already APPROVED and has not being changed so  
        // deal with possible harvestNow option  
        if (aTarget.getState() == Target.STATE_APPROVED && wasHarvestNowSelected) {
            TargetInstance ti = new TargetInstance();
            ti.setTarget(aTarget);
            ti.setSchedule(null);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 5);
            ti.setScheduledTime(cal.getTime());
            ti.setOwner(aTarget.getOwner());
            ti.setUseAQA(aTarget.isUseAQA());
            targetInstanceDao.save(ti);
        }
    }
    if (originalTarget != null) {
        if (!originalTarget.getOwnerOid().equals(aTarget.getOwningUser().getOid())) {
            //carry out notification of the target delegation  
            intrayManager.generateNotification(aTarget.getOwningUser().getOid(), MessageType.CATEGORY_MISC, MessageType.DELEGATE_TARGET, aTarget);
            // If ownership was transferred by someone other than the original owner, let the original  
            // owner know.  
            if (originalTarget.getOwnerOid() != AuthUtil.getRemoteUserObject().getOid()) {
                intrayManager.generateNotification(originalTarget.getOwnerOid(), MessageType.CATEGORY_MISC, MessageType.TRANSFER_TARGET, aTarget);
            }
        }
    }
    // Stay in a schedulable state. Process all schedules  
    if (aTarget.isSchedulable()) {
        log.debug("Staying in Schedulable State - scheduling new schedules");
        for (Schedule schedule : aTarget.getSchedules()) {
            //targetInstances.addAll(createTargetInstances(aTarget, schedule, false));  
            processSchedule(schedule);
        }
    } else {
        unschedule(aTarget);
    }
    // if this target record is in original state 'approved' and the state  
    // is not changing and the target has no active TIs remaining (scheduled,  
    // queued, running, paused, stopping) and updates to the schedules have  
    // caused the removal of all scheduled TIs for this target then set the   
    // status of the target to 'complete'.  
    if (numActiveTIsPrevious > 0 && targetInstanceDao.countActiveTIsForTarget(aTarget.getOid()) == 0 && aTarget.getOriginalState() == Target.STATE_APPROVED && aTarget.getState() == Target.STATE_APPROVED) {
        aTarget.changeState(Target.STATE_COMPLETED);
    }
    // Send notification if someone other than the owner has added  
    // a schedule to this target.  
    if (newSchedulesAddedByNonOwner) {
        intrayManager.generateNotification(aTarget.getOwningUser().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_SCHEDULE_ADDED, aTarget);
    }
    // We may need update the state of some of our parents.  
    updateTargetGroupStatus(aTarget);
    // We also need to handle the updating of any groups that we used to belong to.  
    if (parents != null) {
        for (GroupMemberDTO dto : parents) {
            if (dto.getSaveState() == SAVE_STATE.DELETED) {
                updateTargetGroupStatus(targetDao.loadGroup(dto.getParentOid()));
            }
        }
    }
    // Propagate the group events.  
    if (parents != null) {
        for (GroupMemberDTO dto : parents) {
            switch(dto.getSaveState()) {
                case NEW:
                    TargetGroup grp = targetDao.loadGroup(dto.getParentOid());
                    GroupEventPropagator gep = new GroupEventPropagator(this, instanceManager, grp, aTarget);
                    gep.runEventChain();
                    break;
                case DELETED:
                    TargetGroup grp2 = targetDao.loadGroup(dto.getParentOid());
                    MembersRemovedEventPropagator mrep = new MembersRemovedEventPropagator(this, instanceManager, grp2, aTarget);
                    mrep.runEventChain();
                    break;
            }
        }
    }
    // Send emails if necessary  
    if (sendGroupUpdateNotifications && AuthUtil.getRemoteUserObject() != null) {
        String userName = AuthUtil.getRemoteUserObject().getNiceName();
        // Get the new set of parents.  
        Set<AbstractTargetDTO> newParents = targetDao.getAncestorDTOs(aTarget.getOid());
        // Determine the added parents and removed parents.  
        Set<AbstractTargetDTO> addedParents = new HashSet<AbstractTargetDTO>(newParents);
        addedParents.removeAll(origParents);
        Set<AbstractTargetDTO> removedParents = new HashSet<AbstractTargetDTO>(origParents);
        removedParents.removeAll(newParents);
        HashMap<Long, GroupChangeNotification> changes = new HashMap<Long, GroupChangeNotification>();
        // Arrange the changes into a per user list.  
        for (AbstractTargetDTO dto : addedParents) {
            GroupChangeNotification gcn = null;
            if (!changes.containsKey(dto.getOwnerOid())) {
                gcn = new GroupChangeNotification();
                gcn.userOid = dto.getOwnerOid();
                changes.put(dto.getOwnerOid(), gcn);
            }
            gcn = changes.get(dto.getOwnerOid());
            gcn.addedTo.add(dto);
        }
        for (AbstractTargetDTO dto : removedParents) {
            GroupChangeNotification gcn = null;
            if (!changes.containsKey(dto.getOwnerOid())) {
                gcn = new GroupChangeNotification();
                gcn.userOid = dto.getOwnerOid();
                changes.put(dto.getOwnerOid(), gcn);
            }
            gcn = changes.get(dto.getOwnerOid());
            gcn.removedFrom.add(dto);
        }
        // For each member of the changes map.  
        for (Long userOid : changes.keySet()) {
            // Now construct a message for the user.  
            GroupChangeNotification gcn = changes.get(userOid);
            StringBuffer buff = new StringBuffer();
            buff.append("<P>" + userName + " has modified the group membership of the Target " + aTarget.getName() + ".</p>");
            if (!gcn.addedTo.isEmpty()) {
                buff.append("<p>" + aTarget.getName() + " has been explicitly or implicitly added to the following of your groups:</p>");
                Iterator<AbstractTargetDTO> it = gcn.addedTo.iterator();
                buff.append("<ul>");
                while (it.hasNext()) {
                    buff.append("<li>" + it.next().getName() + "</li>");
                }
                buff.append("</ul>");
            }
            if (!gcn.removedFrom.isEmpty()) {
                buff.append("<p>" + aTarget.getName() + " has been explicitly or implicitly removed from the following of your groups:</p>");
                Iterator<AbstractTargetDTO> it = gcn.removedFrom.iterator();
                buff.append("<ul>");
                while (it.hasNext()) {
                    buff.append("<li>" + it.next().getName() + "</li>");
                }
                buff.append("</ul>");
            }
            // Now send the message.  
            intrayManager.generateNotification(gcn.userOid, MessageType.CATEGORY_MISC, "Target Group membership updated", buff.toString());
        }
    }
}
