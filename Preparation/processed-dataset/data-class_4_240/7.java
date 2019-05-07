public void afterProcessingTimeFrames() {
    for (int i = 0; i < myActivitiesOutOfView.size(); i++) {
        TaskActivity next = (TaskActivity) myActivitiesOutOfView.get(i);
        Integer nextOrdNumber = (Integer) myActivity2ordinalNumber.get(next);
        int topy = nextOrdNumber.intValue() * getRowHeight() + 4;
        // JA Added 
        GraphicPrimitiveContainer container = getContainerFor(next.getTask());
        Rectangle rectangle = container.createRectangle(-10, topy, 1, getRowHeight());
        container.bind(rectangle, next);
    }
    for (int i = 0; i < myVisibleActivities.size(); i++) {
        TaskActivity next = (TaskActivity) myVisibleActivities.get(i);
        Integer nextOrdNumber = (Integer) myActivity2ordinalNumber.get(next);
        int topy = nextOrdNumber.intValue() * getRowHeight() + 4;
        // JA Added 
        GraphicPrimitiveContainer container = getContainerFor(next.getTask());
        Rectangle rectangle = container.createRectangle(getWidth() + 10, topy, 1, getRowHeight());
        container.bind(rectangle, next);
    }
    if (myDependenciesRenderingEnabled) {
        createDependencyLines();
    }
}
