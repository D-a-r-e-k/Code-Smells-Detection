public void save(TargetGroup aTargetGroup, List<GroupMemberDTO> parents) {
    // First check if the TargetGroup type has changed from many-sip  
    // to one-sip or vice versa. If so, unschedule the group as the   
    // scheduling mechanism is entirely different.  
    boolean sipTypeChanged = false;
    if (!aTargetGroup.isNew()) {
        Integer sipType = targetDao.loadPersistedGroupSipType(aTargetGroup.getOid());
        if (sipType.intValue() != aTargetGroup.getSipType()) {
            sipTypeChanged = true;
        }
    }
    // Deal with removed schedules  
    for (Schedule schedule : aTargetGroup.getRemovedSchedules()) {
        log.debug("Removing schedule: " + schedule.getCronPattern());
        targetInstanceDao.deleteScheduledInstances(schedule);
    }
    // Load the original target group so we can do auditing afterwards.  
    AbstractTargetDTO originalTargetGroup = null;
    if (!aTargetGroup.isNew()) {
        originalTargetGroup = targetDao.loadAbstractTargetDTO(aTargetGroup.getOid());
    }
    List<GroupMemberDTO> newChildren = aTargetGroup.getNewChildren();
    Set<Long> removedChildren = aTargetGroup.getRemovedChildren();
    /* ---------------------------------------------------------------- */
    /* Deal with the scheduling state changes                           */
    /* ---------------------------------------------------------------- */
    // Get the original parents  
    Set<AbstractTargetDTO> origParents = Collections.EMPTY_SET;
    if (!aTargetGroup.isNew()) {
        origParents = targetDao.getAncestorDTOs(aTargetGroup.getOid());
    }
    // Save the TargetGroup to the database, along with all the   
    // children.  
    int originalState = aTargetGroup.getOriginalState();
    targetDao.save(aTargetGroup, true, parents);
    // Reload the target group to get all of its children.  
    TargetGroup reloaded = targetDao.reloadTargetGroup(aTargetGroup.getOid());
    if (sipTypeChanged) {
        unschedule(reloaded);
    }
    updateTargetGroupStatus(reloaded);
    // We also need to handle the updating of any groups that we used to belong to.  
    if (parents != null) {
        for (GroupMemberDTO dto : parents) {
            if (dto.getSaveState() == SAVE_STATE.DELETED) {
                updateTargetGroupStatus(targetDao.loadGroup(dto.getParentOid()));
            }
        }
    }
    /* ---------------------------------------------------------------- */
    /* Perform post-save auditing 										*/
    /* ---------------------------------------------------------------- */
    if (originalState != aTargetGroup.getState()) {
        auditor.audit(TargetGroup.class.getName(), aTargetGroup.getOid(), Auditor.ACTION_TARGET_GROUP_STATE_CHANGE, "Target Group " + aTargetGroup.getName() + " has changed into state '" + aTargetGroup.getState() + "'");
    }
    if (originalTargetGroup != null) {
        auditor.audit(TargetGroup.class.getName(), aTargetGroup.getOid(), Auditor.ACTION_UPDATE_TARGET_GROUP, "Target Group " + aTargetGroup.getName() + " has been updated");
        if (!originalTargetGroup.getOwnerOid().equals(aTargetGroup.getOwningUser().getOid())) {
            auditor.audit(TargetGroup.class.getName(), aTargetGroup.getOid(), Auditor.ACTION_TARGET_GROUP_CHANGE_OWNER, "Target Group " + aTargetGroup.getName() + " has been given to " + aTargetGroup.getOwningUser().getNiceName());
        }
        if (!originalTargetGroup.getProfileOid().equals(aTargetGroup.getProfile().getOid())) {
            auditor.audit(TargetGroup.class.getName(), aTargetGroup.getOid(), Auditor.ACTION_TARGET_GROUP_CHANGE_PROFILE, "Target Group " + aTargetGroup.getName() + " is now using profile " + aTargetGroup.getProfile().getName());
        }
    } else {
        auditor.audit(TargetGroup.class.getName(), aTargetGroup.getOid(), Auditor.ACTION_NEW_TARGET_GROUP, "Target Group " + aTargetGroup.getName() + " has been created");
    }
    // Adjust the new/removed lists to make ignore new members that were  
    // also removed (since these were never persisted).  
    List<AbstractTarget> newMembers = new LinkedList<AbstractTarget>();
    for (GroupMemberDTO dto : newChildren) {
        if (removedChildren.contains(dto.getChildOid())) {
            removedChildren.remove(dto.getChildOid());
        } else {
            newMembers.add(targetDao.loadAbstractTarget(dto.getChildOid()));
        }
    }
    // Create a list of the targets that have been removed.  
    List<AbstractTarget> removedTargets = new LinkedList<AbstractTarget>();
    for (Long childOid : removedChildren) {
        removedTargets.add(loadAbstractTarget(childOid));
    }
    GroupEventPropagator gep = new GroupEventPropagator(this, instanceManager, reloaded, newMembers);
    gep.runEventChain();
    MembersRemovedEventPropagator mrep = new MembersRemovedEventPropagator(this, instanceManager, reloaded, removedTargets);
    mrep.runEventChain();
    // Propagate the group events for parents.  
    if (parents != null) {
        for (GroupMemberDTO dto : parents) {
            switch(dto.getSaveState()) {
                case NEW:
                    TargetGroup grp = targetDao.loadGroup(dto.getParentOid());
                    gep = new GroupEventPropagator(this, instanceManager, grp, reloaded);
                    gep.runEventChain();
                    break;
                case DELETED:
                    TargetGroup grp2 = targetDao.loadGroup(dto.getParentOid());
                    mrep = new MembersRemovedEventPropagator(this, instanceManager, grp2, reloaded);
                    mrep.runEventChain();
                    break;
            }
        }
    }
    /* ---------------------------------------------------------------- */
    /* Save the annotations												*/
    /* ---------------------------------------------------------------- */
    // Update the OIDs for the target.  
    for (Annotation anno : aTargetGroup.getAnnotations()) {
        anno.setObjectOid(aTargetGroup.getOid());
    }
    // Save the annotations  
    annotationDAO.saveAnnotations(aTargetGroup.getAnnotations());
    annotationDAO.deleteAnnotations(aTargetGroup.getDeletedAnnotations());
    // Send emails for parent groups if necessary  
    if (sendGroupUpdateNotifications && AuthUtil.getRemoteUserObject() != null) {
        String userName = AuthUtil.getRemoteUserObject().getNiceName();
        // Get the new set of parents.  
        Set<AbstractTargetDTO> newParents = targetDao.getAncestorDTOs(reloaded.getOid());
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
            buff.append("<P>" + userName + " has modified the group membership of the TargetGroup " + reloaded.getName() + ".</p>");
            if (!gcn.addedTo.isEmpty()) {
                buff.append("<p>" + reloaded.getName() + " has been explicitly or implicitly added to the following of your groups:</p>");
                Iterator<AbstractTargetDTO> it = gcn.addedTo.iterator();
                buff.append("<ul>");
                while (it.hasNext()) {
                    buff.append("<li>" + it.next().getName() + "</li>");
                }
                buff.append("</ul>");
            }
            if (!gcn.removedFrom.isEmpty()) {
                buff.append("<p>" + reloaded.getName() + " has been explicitly or implicitly removed from the following of your groups:</p>");
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
    if (sendGroupUpdateNotifications && (removedChildren.size() > 0 || newChildren.size() > 0)) {
        // Generate the email.  
        String userName = AuthUtil.getRemoteUserObject().getNiceName();
        StringBuffer buff = new StringBuffer();
        buff.append("<p>");
        buff.append(userName);
        buff.append(" has updated the group <i>");
        buff.append(aTargetGroup.getName());
        buff.append("</i>.</p>");
        if (newMembers.size() > 0) {
            buff.append("<p>The following members have been added:</p><ul>");
            for (AbstractTarget newMember : newMembers) {
                buff.append("<li>");
                buff.append(newMember.getName());
                buff.append("</li>");
            }
            buff.append("</ul>");
        }
        if (removedTargets.size() > 0) {
            buff.append("<p>The following members have been removed:</p><ul>");
            for (AbstractTarget removedMember : removedTargets) {
                buff.append("<li>");
                buff.append(removedMember.getName());
                buff.append("</li>");
            }
            buff.append("</ul>");
        }
        buff.append("<p>These changes directly or indirectly affect some of your groups as follows:</p>");
        for (User recipient : gep.getUpdatedGroups().keySet()) {
            StringBuffer message = new StringBuffer(buff.toString());
            message.append("<ul>");
            for (TargetGroup group : gep.getUpdatedGroups().get(recipient)) {
                message.append("<li>");
                message.append(group.getName());
                message.append("</li>");
            }
            message.append("</ul>");
            // If recpient is the current user, and only one group updated,  
            // don't send the message.  
            if (!recipient.getOid().equals(AuthUtil.getRemoteUserObject().getOid()) || gep.getUpdatedGroups().get(recipient).size() > 1) {
                intrayManager.generateNotification(recipient.getOid(), MessageType.CATEGORY_MISC, "Target Group membership updated", message.toString());
            }
        }
    }
}
