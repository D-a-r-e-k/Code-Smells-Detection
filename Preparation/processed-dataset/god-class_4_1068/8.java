private boolean isPathExpanded(Task task) {
    boolean result = true;
    TaskContainmentHierarchyFacade taskHierarchy = getChartModel().getTaskManager().getTaskHierarchy();
    for (Task supertask = taskHierarchy.getContainer(task); supertask != null && supertask != getChartModel().getTaskManager().getRootTask(); supertask = taskHierarchy.getContainer(supertask)) {
        if (!supertask.getExpand()) {
            result = false;
            break;
        }
    }
    return result;
}
