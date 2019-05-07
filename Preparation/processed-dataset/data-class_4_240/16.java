private Rectangle processRegularActivity(TaskActivity nextStarted) {
    Task nextTask = nextStarted.getTask();
    if (nextTask.isMilestone() && !nextStarted.isFirst()) {
        return null;
    }
    java.awt.Rectangle nextBounds = getBoundingRectangle(nextStarted);
    int nextLength = (int) nextBounds.width;
    int topy = nextBounds.y;
    topy = topy + (getRowHeight() - 20) / 2;
    if (myModel.isOnlyDown())
        topy = topy - 6;
    else if (myModel.isOnlyUp())
        topy = topy + 6;
    if (myModel.isPrevious())
        topy = topy - 5;
    //        int posX = myPosX; 
    GraphicPrimitiveContainer.Rectangle nextRectangle;
    //        // if (nextStarted.getStart().compareTo(myUnitStart)>=0) { 
    //        TaskLength deltaLength = nextTask.getManager().createLength( 
    //                getChartModel().getTimeUnitStack().getDefaultTimeUnit(), 
    //                myUnitStart, nextStarted.getStart()); 
    // 
    //        int deltaX = (int) (deltaLength.getLength(myCurrentUnit) * getChartModel() 
    //                .getBottomUnitWidth()); 
    //        posX += deltaX; 
    // System.err.println("[TaskRendererImpl] myUnitStart="+myUnitStart+" 
    // nextActivity="+nextStarted+" deltaX="+deltaX+" 
    // deltaLength="+deltaLength.getLength(myCurrentUnit)); 
    // } 
    // else { 
    // nextRectangle = 
    // getPrimitiveContainer().createRectangle(myPosX+getChartModel().getBottomUnitWidth()-nextLength, 
    // topy, nextLength, getRowHeight()*3/5); 
    // } 
    boolean nextHasNested = ((ChartModelImpl) getChartModel()).getTaskContainment().hasNestedTasks(nextTask);
    // JA Switch to 
    GraphicPrimitiveContainer container = getContainerFor(nextTask);
    nextRectangle = container.createRectangle(nextBounds.x, topy, (int) nextLength, 12);
    // CodeReview: why 12, not 15? 
    // System.err.println("task="+nextStarted.getTask()+" nested tasks 
    // length="+nextStarted.getTask().getNestedTasks().length); 
    if (nextStarted.getTask().isMilestone()) {
        nextRectangle.setStyle("task.milestone");
    } else if (nextTask.isProjectTask()) {
        nextRectangle.setStyle("task.projectTask");
        if (nextStarted.isFirst()) {
            // CodeReview: why 12, not 15? 
            GraphicPrimitiveContainer.Rectangle supertaskStart = container.createRectangle(nextRectangle.myLeftX, topy, (int) nextLength, 12);
            supertaskStart.setStyle("task.projectTask.start");
        }
        if (nextStarted.isLast()) {
            GraphicPrimitiveContainer.Rectangle supertaskEnd = container.createRectangle(myPosX - 1, topy, (int) nextLength, 12);
            supertaskEnd.setStyle("task.projectTask.end");
        }
    } else if (nextHasNested) {
        nextRectangle.setStyle("task.supertask");
        if (nextStarted.isFirst()) {
            // CodeReview: why 12, not 15? 
            GraphicPrimitiveContainer.Rectangle supertaskStart = container.createRectangle(nextRectangle.myLeftX, topy, (int) nextLength, 12);
            supertaskStart.setStyle("task.supertask.start");
        }
        if (nextStarted.isLast()) {
            // CodeReview: why 12, not 15? 
            GraphicPrimitiveContainer.Rectangle supertaskEnd = container.createRectangle(nextRectangle.myLeftX, topy, (int) nextLength, 12);
            supertaskEnd.setStyle("task.supertask.end");
        }
    } else if (nextStarted.getIntensity() == 0f) {
        nextRectangle.setStyle("task.holiday");
    } else {
        if (nextStarted.isFirst() && nextStarted.isLast()) {
            nextRectangle.setStyle("task.startend");
        } else if (false == nextStarted.isFirst() ^ nextStarted.isLast()) {
            nextRectangle.setStyle("task");
        } else if (nextStarted.isFirst()) {
            nextRectangle.setStyle("task.start");
        } else if (nextStarted.isLast()) {
            nextRectangle.setStyle("task.end");
        }
    }
    if (myProgressRenderingEnabled && !nextTask.isMilestone() && !nextHasNested) {
        renderProgressBar(nextStarted, nextRectangle);
    }
    if (!"task.holiday".equals(nextRectangle.getStyle()) && !"task.supertask".equals(nextRectangle.getStyle())) {
        nextRectangle.setBackgroundColor(nextStarted.getTask().getColor());
    }
    container.bind(nextRectangle, nextStarted);
    return nextRectangle;
}
