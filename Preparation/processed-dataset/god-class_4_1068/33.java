/**
     * @return
     */
private List prepareDependencyDrawData() {
    List result = new ArrayList();
    List /* <Task> */
    visibleTasks = ((ChartModelImpl) getChartModel()).getVisibleTasks();
    for (int i = 0; i < visibleTasks.size(); i++) {
        if (visibleTasks.get(i).equals(BlankLineNode.BLANK_LINE))
            continue;
        // todo a revoir... 
        Task nextTask = (Task) visibleTasks.get(i);
        if (nextTask != null)
            prepareDependencyDrawData(nextTask, result);
    }
    return result;
}
