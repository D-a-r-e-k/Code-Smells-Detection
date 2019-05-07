/**
     * This method is called recursively to process the task hierarchy and
     * create the equivalent data structure in GanttProject.
     * 
     * @param defaultCalendar
     *            BaseCalendar instance
     * @param task
     *            Parent task
     * @param node
     *            Parent node
     * @throws Exception
     */
private void processTask(TaskManager tm, MPXCalendar defaultCalendar, Task task, DefaultMutableTreeNode node) throws Exception {
    // 
    // Calculate the duration in days 
    // 
    MPXCalendar taskCalendar = task.getCalendar();
    MPXCalendar cal;
    if (taskCalendar != null) {
        cal = taskCalendar;
    } else {
        cal = defaultCalendar;
    }
    final MPXDuration duration;
    boolean milestone = task.getMilestoneValue();
    if (milestone == true) {
        duration = MILESTONE_DURATION;
    } else {
        Date taskStart = task.getStart();
        Date taskFinish = task.getFinish();
        if (taskStart != null && taskFinish != null) {
            //duration = cal.getDuration(taskStart, taskFinish); 
            duration = task.getDuration();
        } else {
            duration = task.getDuration();
        }
    }
    // 
    // Create the new task object 
    // 
    GanttTask gtask = tm.createTask();
    // gtask.setChecked(); 
    // gtask.setColor(); 
    gtask.setCompletionPercentage((int) task.getPercentageCompleteValue());
    // gtask.setExpand() 
    // gtask.setLength(); 
    gtask.setMilestone(milestone);
    gtask.setName(task.getName() == null ? "-" : task.getName());
    gtask.setNotes(task.getNotes());
    Priority prio = task.getPriority();
    if (prio != null) {
        int priority = prio.getValue();
        int p;
        switch(priority) {
            case Priority.HIGHEST:
            case Priority.HIGHER:
            case Priority.VERY_HIGH:
                p = 2;
                break;
            case Priority.LOWEST:
            case Priority.LOWER:
            case Priority.VERY_LOW:
                p = 0;
                break;
            default:
                p = 1;
        }
        gtask.setPriority(p);
    }
    // gtask.setShape(); 
    // gtask.setStartFixed() 
    // gtask.setTaskID() 
    gtask.setWebLink(task.getHyperlink());
    Date taskStart = task.getStart();
    assert taskStart != null : "Task=" + task + " has null start";
    gtask.setStart(new GanttCalendar(taskStart));
    //        gtask.setDuration(tm.createLength((long) duration.getDuration())); 
    long longDuration = (long) Math.ceil(duration.convertUnits(TimeUnit.DAYS).getDuration());
    if (longDuration > 0) {
        gtask.setDuration(tm.createLength(longDuration));
    } else {
        System.err.println("Task " + task.getName() + " has duration=" + duration + " which is 0 as long integer. This duration has been ignored, task has got the default duration");
    }
    // gtask.setEnd(new GanttCalendar(task.getFinish())); 
    // 
    // Add the task and process any child tasks 
    // 
    tm.registerTask(gtask);
    m_tasks.addObject(gtask, (TaskNode) node, -1);
    m_taskMap.put(task.getID(), new Integer(gtask.getTaskID()));
    LinkedList children = task.getChildTasks();
    if (children.size() != 0) {
        node = m_tasks.getNode(gtask.getTaskID());
        Iterator iter = children.iterator();
        while (iter.hasNext() == true) {
            processTask(tm, defaultCalendar, (Task) iter.next(), node);
        }
    }
}
