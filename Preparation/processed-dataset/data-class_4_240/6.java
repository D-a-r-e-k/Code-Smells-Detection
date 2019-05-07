public void beforeProcessingTimeFrames() {
    getPrimitiveContainer().clear();
    getPrimitiveContainer().getLayer(1).clear();
    getPrimitiveContainer().getLayer(2).clear();
    myActivity2ordinalNumber.clear();
    myTask_WorkingRectanglesLength.clear();
    myActivitiesOutOfView.clear();
    myVisibleActivities = getSortedTaskActivities();
    if (myTasks != null) {
        myPreviousStateTasks = (ArrayList) myTasks.clone();
    }
    myCurrentlyProcessed.clear();
    myPosX = 0;
}
