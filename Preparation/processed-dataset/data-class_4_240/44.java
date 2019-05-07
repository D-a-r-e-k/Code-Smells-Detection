private GraphicPrimitiveContainer getContainerFor(Task task) {
    boolean hasNested = ((ChartModelImpl) getChartModel()).getTaskContainment().hasNestedTasks(task);
    // JA Switch to 
    return hasNested ? getPrimitiveContainer().getLayer(2) : getPrimitiveContainer();
}
