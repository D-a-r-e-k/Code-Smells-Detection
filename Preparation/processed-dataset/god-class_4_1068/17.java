private void renderProgressBar(TaskActivity nextStarted, GraphicPrimitiveContainer.Rectangle nextActivityRectangle) {
    if (nextStarted.getIntensity() == 0) {
        return;
    }
    Task nextTask = nextStarted.getTask();
    int nextLength = nextActivityRectangle.myWidth;
    Long workingRectanglesLength = (Long) myTask_WorkingRectanglesLength.get(nextTask);
    if (workingRectanglesLength != null) {
        long nextProgressLength = nextLength;
        String style;
        if (workingRectanglesLength.longValue() > nextLength) {
            myTask_WorkingRectanglesLength.put(nextTask, new Long(workingRectanglesLength.longValue() - nextLength));
            style = "task.progress";
        } else {
            nextProgressLength = workingRectanglesLength.longValue();
            myTask_WorkingRectanglesLength.remove(nextTask);
            style = "task.progress.end";
        }
        int nextMidY = nextActivityRectangle.getMiddleY();
        GraphicPrimitive nextProgressRect = getPrimitiveContainer().getLayer(1).createRectangle(nextActivityRectangle.myLeftX, nextMidY - 1, (int) nextProgressLength, 3);
        nextProgressRect.setStyle(style);
        getPrimitiveContainer().getLayer(1).bind(nextProgressRect, nextTask);
    }
}
