private void pullQueue(Date unitStart, Date unitFinish) {
    for (Iterator activities = myVisibleActivities.iterator(); activities.hasNext(); ) {
        TaskActivity next = (TaskActivity) activities.next();
        if (next.getEnd().before(getChartModel().getStartDate())) {
            myActivitiesOutOfView.add(next);
            activities.remove();
            continue;
        }
        if (next.getStart().before(getChartModel().getStartDate()) && next.getEnd().after(getChartModel().getEndDate())) {
            myCurrentlyProcessed.add(next);
            activities.remove();
            continue;
        }
        if (next.getStart().after(unitFinish)) {
            break;
        }
        if (next.getStart().compareTo(unitStart) >= 0 && next.getStart().compareTo(unitFinish) < 0 || next.getEnd().compareTo(unitStart) >= 0 && next.getEnd().compareTo(unitFinish) < 0) {
            //System.err.println("pullQueue: \nnextActivity="+next+"\ntask="+next.getTask()+" \nunitStart="+unitStart+" unitFinish="+unitFinish); 
            myCurrentlyProcessed.add(next);
            activities.remove();
        }
    }
    // initialize the myPreviousCurrentlyProcessed List 
    // each index value matches with the row 
    // null value means there is no previous task for this row or 
    // the previous task is not between unitStart & unitFinish 
    if (myModel.isPrevious()) {
        List visibleTasks = ((ChartModelImpl) getChartModel()).getVisibleTasks();
        for (int i = 0; i < visibleTasks.size(); i++) {
            Task task = (Task) visibleTasks.get(i);
            int index = getPreviousStateTaskIndex(task);
            GPCalendar calendar = myModel.getTaskManager().getCalendar();
            if (index != -1) {
                GanttPreviousStateTask previousStateTask = (GanttPreviousStateTask) myPreviousStateTasks.get(index);
                previousStateTask.setState(task, calendar);
                if (previousStateTask.getStart().after(unitFinish)) {
                    myPreviousStateCurrentlyProcessed.add(null);
                } else if (previousStateTask.getStart().getTime().compareTo(unitStart) >= 0 && previousStateTask.getStart().getTime().compareTo(unitFinish) < 0) // )|| previousStateTask.getEnd( 
                // calendar) 
                // .getTime().compareTo(unitStart) > 0 
                // && previousStateTask.getEnd( 
                // calendar) 
                // .getTime().compareTo(unitFinish) < 0) 
                {
                    myPreviousStateCurrentlyProcessed.add(previousStateTask);
                    myPreviousStateTasks.remove(index);
                } else if (previousStateTask.getStart().getTime().compareTo(unitStart) < 0 && (previousStateTask.getEnd(calendar).getTime().compareTo(unitStart) > 0)) {
                    GanttCalendar newStart = new GanttCalendar(unitStart);
                    int id = previousStateTask.getId();
                    int duration = previousStateTask.getDuration() - newStart.diff(previousStateTask.getStart());
                    int diff = newStart.diff(previousStateTask.getStart());
                    for (int j = 0; j < diff; j++) {
                        if (calendar.isNonWorkingDay(previousStateTask.getStart().newAdd(j).getTime())) {
                            duration++;
                        }
                    }
                    boolean isMilestone = previousStateTask.isMilestone();
                    boolean hasNested = previousStateTask.hasNested();
                    GanttPreviousStateTask partOfPreviousTask = new GanttPreviousStateTask(id, newStart, duration, isMilestone, hasNested);
                    partOfPreviousTask.setState(task, calendar);
                    partOfPreviousTask.setIsAPart(true);
                    myPreviousStateCurrentlyProcessed.add(partOfPreviousTask);
                    myPreviousStateTasks.remove(index);
                } else
                    myPreviousStateCurrentlyProcessed.add(null);
            } else
                myPreviousStateCurrentlyProcessed.add(null);
        }
    }
}
