private List getSortedTaskActivities() {
    List visibleTasks = ((ChartModelImpl) getChartModel()).getVisibleTasks();
    List visibleActivities = new ArrayList();
    myActivity2ordinalNumber.clear();
    for (int i = 0; i < visibleTasks.size(); i++) {
        if (visibleTasks.get(i).equals(BlankLineNode.BLANK_LINE))
            continue;
        // todo a revoir... 
        Task nextTask = (Task) visibleTasks.get(i);
        Integer nextOrdinal = new Integer(i);
        if (nextTask == null) {
            continue;
        }
        TaskActivity[] activities = nextTask.getActivities();
        //System.err.println("[TaskRendererImpl]task="+nextTask+"\nactivities="+java.util.Arrays.asList(activities)); 
        float totalTaskLength = 0;
        for (int j = 0; j < activities.length; j++) {
            final TaskActivity nextActivity = activities[j];
            myActivity2ordinalNumber.put(nextActivity, nextOrdinal);
            visibleActivities.add(nextActivity);
            if (!nextActivity.getEnd().before(getChartModel().getStartDate()) && nextActivity.getIntensity() > 0) {
                totalTaskLength += activities[j].getDuration().getLength(getChartModel().getBottomUnit()) * getChartModel().getBottomUnitWidth();
            }
        }
        myTask_WorkingRectanglesLength.put(nextTask, new Long((long) (totalTaskLength * nextTask.getCompletionPercentage() / 100)));
    }
    Set hashedVisible = new HashSet(visibleActivities);
    Integer maxOrdinal = new Integer(hashedVisible.size() + 1);
    Integer minOrdinal = new Integer(-2);
    for (int i = 0; i < visibleTasks.size(); i++) {
        Task next = (Task) visibleTasks.get(i);
        TaskDependency[] dependencies = next.getDependenciesAsDependant().toArray();
        for (int j = 0; j < dependencies.length; j++) {
            TaskDependency nextDependency = dependencies[j];
            TaskActivity dependeeActivity = nextDependency.getActivityBinding().getDependeeActivity();
            if (hashedVisible.contains(dependeeActivity)) {
                continue;
            }
            Task dependeeTask = dependeeActivity.getTask();
            if (false == getChartModel().getTaskManager().getTaskHierarchy().contains(dependeeTask)) {
                continue;
            }
            if (false == isPathExpanded(dependeeTask)) {
                continue;
            }
            int diff = getChartModel().getTaskManager().getTaskHierarchy().compareDocumentOrder(next, dependeeTask);
            assert diff != 0;
            Integer dependeePosition = diff < 0 ? maxOrdinal : minOrdinal;
            myActivity2ordinalNumber.put(dependeeActivity, dependeePosition);
            visibleActivities.add(dependeeActivity);
            hashedVisible.add(dependeeActivity);
        }
        dependencies = next.getDependenciesAsDependee().toArray();
        for (int j = 0; j < dependencies.length; j++) {
            TaskDependency nextDependency = dependencies[j];
            TaskActivity dependantActivity = nextDependency.getActivityBinding().getDependantActivity();
            if (hashedVisible.contains(dependantActivity)) {
                continue;
            }
            Task dependantTask = dependantActivity.getTask();
            if (false == getChartModel().getTaskManager().getTaskHierarchy().contains(dependantTask)) {
                continue;
            }
            if (false == isPathExpanded(dependantTask)) {
                continue;
            }
            int diff = getChartModel().getTaskManager().getTaskHierarchy().compareDocumentOrder(next, dependantTask);
            assert diff != 0;
            Integer dependantPosition = diff < 0 ? maxOrdinal : minOrdinal;
            myActivity2ordinalNumber.put(dependantActivity, dependantPosition);
            visibleActivities.add(dependantActivity);
            hashedVisible.add(dependantActivity);
        }
    }
    ourAlgorithm.sortByStartDate(visibleActivities);
    return visibleActivities;
}
