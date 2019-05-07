public void nextTimeUnit(int unitIndex) {
    if (myCurrentUnit != null) {
        Date unitStart = myCurrentTimeFrame.getUnitStart(myCurrentUnit, unitIndex);
        Date unitFinish = myCurrentTimeFrame.getUnitFinish(myCurrentUnit, unitIndex);
        myUnitStart = unitStart;
        pullQueue(unitStart, unitFinish);
        // System.err.println("[TaskRendererImpl] nextTimeUnit(): 
        // unitStart="+unitStart+" posX="+myPosX); 
        // if (!myCurrentlyProcessed.isEmpty()) { 
        // System.err.println("[TaskRendererImpl] nextTimeUnit(): 
        // processing:"+myCurrentlyProcessed); 
        // } 
        for (Iterator startedActivities = myCurrentlyProcessed.iterator(); startedActivities.hasNext(); startedActivities.remove()) {
            TaskActivity nextStarted = (TaskActivity) startedActivities.next();
            processActivity(nextStarted);
        }
        if (myModel.isPrevious()) {
            for (int i = 0; i < myPreviousStateCurrentlyProcessed.size(); i++) {
                Object next = myPreviousStateCurrentlyProcessed.get(i);
                // System.out.println (next + " : " + i); 
                if (next != null) {
                    GanttPreviousStateTask previousTask = (GanttPreviousStateTask) next;
                    drawPreviousStateTask(previousTask, i);
                }
            }
            myPreviousStateCurrentlyProcessed = new ArrayList();
        }
        myPosX += getChartModel().getBottomUnitWidth();
    }
}
